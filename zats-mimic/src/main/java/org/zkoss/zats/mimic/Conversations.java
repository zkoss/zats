/* Conversations.java

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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.EmulatorConversation;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;

/**
 * To manage ServerEmulator and Conversation  
 * @author Hawk
 *
 */
public class Conversations {
	private static List<Conversation> conversations = new LinkedList<Conversation>();
	
	private static Emulator emulator ;
	private static Logger logger = Logger.getLogger(Conversations.class.getName());;


	/**
	 * to start a server emulator, it will stop previous one first. 
	 * @param resourceRoot
	 * @return Emulator
	 */
		public static Emulator start(String resourceRoot){
		stop();
		// prepare environment
		String tmpDir = System.getProperty("java.io.tmpdir", ".");
		File webinf = new File(tmpDir, "zats/" + System.currentTimeMillis()
				+ "/WEB-INF");
		if (!webinf.mkdirs())
			throw new ConversationException("can't create temp directory");
		File web = webinf.getParentFile();
		InputStream src = EmulatorConversation.class.getResourceAsStream("WEB-INF/zk.xml");
		File dest = new File(web, "WEB-INF/zk.xml");
		OutputStream os = null;
		try {
			os = new FileOutputStream(dest);
			byte[] buf = new byte[65536];
			int len;
			while (true) {
				len = src.read(buf);
				if (len < 0)
					break;
				os.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new ConversationException("fail to copy file", e);
		} finally {
			close(src);
			close(os);
		}
		// TODO clean directories
		dest.deleteOnExit();
		webinf.deleteOnExit();
		
		emulator = new EmulatorBuilder(web)
		.addResource(resourceRoot)
		.descriptor(EmulatorConversation.class.getResource("WEB-INF/web.xml")).create();
		
		return emulator;
	}
	

	private static void close(Closeable c) {
		try {
			c.close();
		} catch (Throwable e) {
			logger.severe("Cannot close" +c);
		}
	}
	
	public static void stop() {
		if (emulator!=null){
			emulator.close();
		}
	}

	/**
	 * Open a new conversation.
	 * @return
	 */
	public static Conversation open(){
		Conversation conversation = ConversationBuilder.create(emulator);
		conversations.add(conversation);
		return conversation;
	}
	
	/**
	 * close all conversations and release resources.
	 */
	public static void closeAll() {
		for (Conversation c : conversations){
			((ConversationCtrl)c).close();
		}
		conversations.clear();
	}
	

	/**
	 * get last open conversation's desktop.
	 * 
	 * @return desktop.
	 */
	public static DesktopAgent getDesktop() {
		if(conversations.isEmpty()){
			throw new ConversationException("not in a running converation");
		}else{
			return conversations.get(conversations.size()-1).getDesktop();
		}
	}

	/**
	 * last open conversation's session
	 * 
	 * @return session or null if it doesn't have.
	 */
	public static HttpSession getSession() {
		if(conversations.isEmpty()){
			throw new ConversationException("not in a running converation");
		}else{
			return conversations.get(conversations.size()-1).getSession();
		}
		
	}

}
