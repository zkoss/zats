/* Conversations.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;

import javax.servlet.http.HttpSession;

public class Conversations {
	private static ThreadLocal<Conversation> local = new ThreadLocal<Conversation>();

	/**
	 * to start a test conversation, it will close previous conversation if previous one is running. 
	 * @param resourceRoot
	 * @return the conversation
	 */
	public static Conversation start(String resourceRoot) {
		stop();
		try {
			local.set(ConversationBuilder.create());
			local.get().start(resourceRoot);
			return local.get();
		} catch (Throwable e) {
			local.remove();
			throw new ConversationException(e.getMessage(), e);
		}
	}

	/**
	 * close the last conversation and release resources.
	 */
	public static void stop() {
		if (local.get() == null)
			return;
		local.get().stop();
		local.remove();
	}

	/**
	 * navigate last conversation to the zul file
	 * @param zulPath the path of zul file.
	 * @return the desktop
	 */
	public static DesktopAgent open(String zulPath) {
		if (local.get() == null)
			throw new ConversationException("not in a running converation");
		return local.get().open(zulPath);
	}

	/**
	 * clean the last desktop
	 */
	public static void clean() {
		if (local.get() == null)
			return;
		local.get().clean();
	}

	/**
	 * get the desktop from specify zul.
	 * 
	 * @return desktop.
	 */
	public static DesktopAgent getDesktop() {
		if (local.get() == null)
			throw new ConversationException("not in a running converation");
		return local.get().getDesktop();
	}

	/**
	 * get the session.
	 * 
	 * @return session or null if it doesn't have.
	 */
	public static HttpSession getSession() {
		if (local.get() == null)
			throw new ConversationException("not in a running converation");
		return local.get().getSession();
	}
	
	/**
	 * to find the a component agent with the selector in last desktop in last conversation
	 * @param selector the selector
	 * @return the first component agent, null if not found
	 */
	public static ComponentAgent query(String selector){
		if (local.get() == null)
			throw new ConversationException("not in a running converation");
		return local.get().query(selector);
	}
	
	/**
	 * to find component agents with the selector in in last desktop in last conversation
	 * @param selector the selector
	 * @return the component agents
	 */
	public static List<ComponentAgent> queryAll(String selector){
		if (local.get() == null)
			throw new ConversationException("not in a running converation");
		return local.get().queryAll(selector);
	}
}
