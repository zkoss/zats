/* ZatsContext.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;

/**
 * A default implementation of ZatsContext
 *   
 * @author Hawk
 * @author Dennis
 */
public class DefaultZatsContext implements ZatsContext{
	private static Logger logger = Logger.getLogger(DefaultZatsContext.class.getName());;
	
	
	private List<Client> clients = new LinkedList<Client>();
	private Emulator emulator ;
	
	//keep the file should be clean when destroying.
	private List<File> tempFiles = new ArrayList<File>();

	public void init(String resourceRoot){
		if(emulator!=null) {
			throw new ZatsException("already started up");
		}
		// prepare environment
		String tmpDir = System.getProperty("java.io.tmpdir", ".");
		File tmpZats = new File(tmpDir,"zats");
		tmpZats.mkdirs();
		
		File webTemp = new File(tmpZats,Long.toHexString(System.currentTimeMillis()));
		webTemp.mkdirs();
		tempFiles.add(0,webTemp);
		
		File webinf = new File(webTemp, "WEB-INF");
		tempFiles.add(0,webinf);
		if (!webinf.mkdirs())
			throw new ZatsException("can't create temp directory : "+webinf);
		
		InputStream src = EmulatorClient.class.getResourceAsStream("WEB-INF/zk.xml");
		File zkxml = new File(webinf, "zk.xml");
		OutputStream os = null;
		try {
			os = new FileOutputStream(zkxml);
			byte[] buf = new byte[65536];
			int len;
			while (true) {
				len = src.read(buf);
				if (len < 0)
					break;
				os.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new ZatsException("fail to copy file", e);
		} finally {
			close(src);
			close(os);
		}
		tempFiles.add(0,zkxml);
		
		emulator = new EmulatorBuilder()
			.addContentRoot(webTemp.getAbsolutePath())
			.addContentRoot(resourceRoot)
			.setDescriptor(EmulatorClient.class.getResource("WEB-INF/web.xml").toString()).create();
		
	}
	

	private static void close(Closeable c) {
		try {
			c.close();
		} catch (Throwable e) {
			logger.severe("Cannot close" +c);
		}
	}
	
	public void destroy() {
		cleanup();
		if (emulator!=null){
			emulator.close();
			emulator=null;
		}
		for(File f:tempFiles){
			if(!f.delete()){
				f.deleteOnExit();
			}
		}
		tempFiles.clear();
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
	 * close all client and release resources.
	 */
	public void cleanup() {
		//to avoid concurrent modification exception in willClose
		for (Client c : clients.toArray(new Client[clients.size()])){
			c.destroy();
		}
	}
	

}
