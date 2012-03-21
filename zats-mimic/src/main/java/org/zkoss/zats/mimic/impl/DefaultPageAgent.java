/* DefaultPageAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.PageAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

public class DefaultPageAgent implements PageAgent {
	private DesktopAgent desktopAgent;
	private Page page;

	public DefaultPageAgent(DesktopAgent desktopAgent, Page page) {
		this.desktopAgent = desktopAgent;
		this.page = page;
	}

	public String getId() {
		return page.getId();
	}

	public String getType() {
		return "page";
	}

	public Object getAttribute(String name) {
		return page.getAttribute(name);
	}

	public Map<String, Object> getAttributes() {
		return page.getAttributes();
	}

	public String getUuid() {
		return page.getUuid();
	}

	public List<ComponentAgent> getChildren() {
		List<ComponentAgent> agents = new ArrayList<ComponentAgent>();
		Iterator<?> iterator = page.getRoots().iterator();
		while (iterator.hasNext())
			agents.add(new DefaultComponentAgent(this, (Component) iterator
					.next()));
		return agents;
	}

	public Conversation getConversation() {
		return desktopAgent.getConversation();
	}

	public DesktopAgent getDesktop() {
		return desktopAgent;
	}

	public Page getPage() {
		return page;
	}

	@Override
	public int hashCode() {
		return page.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return page.equals(obj);
	}
	
	public Object getDelegatee() {
		return page;
	}
	
	public String toString(){
		return new StringBuilder().append(getClass().getSimpleName())
			.append("@").append(Integer.toHexString(System.identityHashCode(this)))
			.append("[").append(page.toString()).append("]")
			.toString();
	}
}
