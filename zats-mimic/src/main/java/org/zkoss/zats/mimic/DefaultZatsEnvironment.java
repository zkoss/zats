/* DefaultZatsEnvironment.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.zkoss.idom.Document;
import org.zkoss.idom.Element;
import org.zkoss.idom.input.SAXBuilder;
import org.zkoss.io.Files;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;

/**
 * A default implementation of {@link ZatsEnvironment}
 *   
 * @author Hawk
 * @author Dennis
 */
public class DefaultZatsEnvironment implements ZatsEnvironment{
//	private static Logger logger = Logger.getLogger(DefaultZatsEnvironment.class.getName());;
	
	
	private List<Client> clients = new LinkedList<Client>();
	private Emulator emulator ;
	
	private String webInfPathOrUrl;
	private File tmpWebInfFolder;
	private String contextPath;
	
	/**
	 * Create a zats context, it uses built-in config file(web.xml, zk.xml) to init the context quickly and safely. <br/>
	 */
	public DefaultZatsEnvironment(){
		this(null,null);
	}
	
	/**
	 * Create a zats Environment. <br/>
	 * The webInfPathOrUrl is the folder of the WEB-INF to start the zats environment.  <br/>
	 * @param webInfPathOrUrl the folder of WEB-INF, a null value means use built-in WEB-INF folder.
	 */
	public DefaultZatsEnvironment(String webInfPathOrUrl){
		this.webInfPathOrUrl = webInfPathOrUrl;
	}
	
	/**
	 * Create a zats Environment. <br/>
	 * The webInfPathOrUrl is the folder of the WEB-INF to start the zats environment, for example "./src/test/resources/web/WEB-INF". <br/>
	 * The contextPath is the path of the application context, for example "/" or "/myapp". <br/>
	 * @param webInfPathOrUrl the folder of WEB-INF, a null value means suing built-in WEB-INF folder.
	 * @param contextPath the name of the application, a null value means using "/"
	 */
	public DefaultZatsEnvironment(String webInfPathOrUrl,String contextPath){
		this.webInfPathOrUrl = webInfPathOrUrl;
		this.contextPath = contextPath;
	}

	public void init(String resourceRoot){
		if(emulator!=null) {
			throw new ZatsException("already started up");
		}
		if(webInfPathOrUrl==null){
			URL weburl = EmulatorClient.class.getResource("WEB-INF/web.xml");
			if(weburl==null){
				throw new IllegalStateException("built-in web.xml not found");
			}

			webInfPathOrUrl = weburl.toExternalForm();
			//the web-info url
			webInfPathOrUrl = webInfPathOrUrl.substring(0,webInfPathOrUrl.lastIndexOf('/')+1);
		}
		makeTmpWebInf();
		
		EmulatorBuilder builder = new EmulatorBuilder();
		builder.setWebInf(webInfPathOrUrl);
		builder.setContextPath(contextPath==null?"/":contextPath);
		builder.addContentRoot(resourceRoot);
		emulator = builder.create();
	}
	
	/**
	 * In order to catch exception from zk ExecutionCleanup, we copy the WEB-INF dir
	 * to tmp dir while the location is given from java.io.tmpdir 
	 * and then add one more config into zk.xml
	 * @param weburl 
	 */
	private void makeTmpWebInf() {
		PrintWriter writer = null;
		try {
			//copy whole WEB-INF dir to tmp
			String os = System.getProperty("os.name").toLowerCase();
			webInfPathOrUrl = os.indexOf("win") >= 0 ? webInfPathOrUrl.replace("file:/", "") : 
				webInfPathOrUrl.replace("file:", "");
			File srcFolder = new File(webInfPathOrUrl);
			
			tmpWebInfFolder = new File(System.getProperty("java.io.tmpdir"), "ZATS-TMP-WEB-INF");
			String tmpWebInfPathOrUrl = tmpWebInfFolder.getAbsolutePath();
			if (tmpWebInfFolder.exists())
				Files.deleteAll(tmpWebInfFolder);
	    	
	    	Files.copy(tmpWebInfFolder, srcFolder, Files.CP_OVERWRITE);
	    	
			//insert one more config
	    	Document zkxml = new SAXBuilder(true, false, true).build(new File(webInfPathOrUrl, "zk.xml"));
			Element el = zkxml.getRootElement();
			Element listener = new Element("listener");
			Element listenerclass = new Element("listener-class");
			listenerclass.setContent("org.zkoss.zats.mimic.exception.ZKExecutionCleanup");
			listener.appendChild(listenerclass);
			el.appendChild(listener);

			//create new zk.xml, delete the original one
			File originZKXml = new File(tmpWebInfFolder, "zk.xml");
			
			if (!originZKXml.delete()) {
				throw new ZatsException("Can't remove zk.xml under " + tmpWebInfPathOrUrl);
			}
			DOMSource domSource = new DOMSource(zkxml);
			writer = new PrintWriter(originZKXml);
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			writer.println();
			
			//point web info dir to the tmp one
			webInfPathOrUrl = tmpWebInfPathOrUrl;
		} catch (Exception e) {
		    throw new ZatsException(e.getMessage(), e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private void deleteTmpWebInf() {
		webInfPathOrUrl = null; // ensure to reset web inf path
		if (tmpWebInfFolder != null && tmpWebInfFolder.exists())
			Files.deleteAll(tmpWebInfFolder);
	}

	public void destroy() {
		cleanup();
		if (emulator!=null){
			emulator.close();
			emulator=null;
		}
		deleteTmpWebInf();
	}

	/**
	 * create a client.
	 * @return a new client
	 */
	public Client newClient(){
		if(emulator==null){
			throw new ZatsException("not initialize yet, please call init first");
		}
		Client client = new EmulatorClient(emulator);
		((ClientCtrl)client).setDestroyListener(new ClientCtrl.DestroyListener() {
			public void willDestroy(Client conv) {
				clients.remove(conv);//just remove it from list, client will destory itself
			}
		});
		clients.add(client);
		return client;
	}
	
	/**
	 * close all clients and release resources.
	 */
	public void cleanup() {
		//to avoid concurrent modification exception in willClose
		for (Client c : clients.toArray(new Client[clients.size()])){
			c.destroy();
		}
	}
	

}
