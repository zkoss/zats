/* DefaultZatsEnvironment.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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
	private static Logger logger = Logger.getLogger(DefaultZatsEnvironment.class.getName());;
	
	
	private List<Client> clients = new LinkedList<Client>();
	private Emulator emulator ;
	
	private String webInfPathOrUrl;
	
	/**
	 * Create a zats context, it uses built-in config file(web.xml, zk.xml) to init the context quickly and safely.
	 */
	public DefaultZatsEnvironment(){
		this(null);
	}
	
	/**
	 * Create a zats Environment. <br/>
	 * The webInfPathOrUrl is the folder of the WEB-INF to start the zats environment. 
	 * @param webInfFolder the folder of WEB-INF, a null value means use built-in WEB-INF folder.
	 */
	public DefaultZatsEnvironment(String webInfPathOrUrl){
		this.webInfPathOrUrl = webInfPathOrUrl;
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
		
		EmulatorBuilder builder = new EmulatorBuilder();
		builder.setWebInf(webInfPathOrUrl);
		builder.addContentRoot(resourceRoot);
		emulator = builder.create();
	}

	
	public void destroy() {
		cleanup();
		if (emulator!=null){
			emulator.close();
			emulator=null;
		}
	}

	/**
	 * create a client.
	 * @return
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
