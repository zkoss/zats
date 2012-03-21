/* DefaultDesktopNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.node.DesktopAgent;
import org.zkoss.zats.mimic.node.PageAgent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;

public class DefaultDesktopAgent implements DesktopAgent {
	private Conversation conversation;
	private Desktop desktop;

	public DefaultDesktopAgent(Conversation conversation, Desktop desktop) {
		this.conversation = conversation;
		this.desktop = desktop;
	}

	public String getId() {
		return desktop.getId();
	}

	public String getType() {
		return "desktop";
	}

	public Object getAttribute(String name) {
		return desktop.getAttribute(name);
	}

	public Map<String, Object> getAttributes() {
		return desktop.getAttributes();
	}

	public Conversation getConversation() {
		return conversation;
	}

	public List<PageAgent> getPages() {
		List<PageAgent> nodes = new ArrayList<PageAgent>();
		Iterator<?> iter = desktop.getPages().iterator();
		while (iter.hasNext())
			nodes.add(new DefaultPageAgent(this, (Page) iter.next()));
		return nodes;
	}

	public Desktop cast() {
		return desktop;
	}

	@Override
	public int hashCode() {
		return desktop.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return desktop.equals(obj);
	}
	
	public String toString(){
		return new StringBuilder().append(getClass().getSimpleName())
			.append("@").append(Integer.toHexString(System.identityHashCode(this)))
			.append("[").append(desktop.toString()).append("]")
			.toString();
	}
}
