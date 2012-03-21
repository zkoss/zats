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
import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.node.DesktopAgent;
import org.zkoss.zats.mimic.node.PageAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

public class DefaultPageAgent implements PageAgent {
	private DesktopAgent desktopNode;
	private Page page;

	public DefaultPageAgent(DesktopAgent desktopNode, Page page) {
		this.desktopNode = desktopNode;
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
		List<ComponentAgent> nodes = new ArrayList<ComponentAgent>();
		Iterator<?> iterator = page.getRoots().iterator();
		while (iterator.hasNext())
			nodes.add(new DefaultComponentAgent(this, (Component) iterator
					.next()));
		return nodes;
	}

	public Conversation getConversation() {
		return desktopNode.getConversation();
	}

	public DesktopAgent getDesktop() {
		return desktopNode;
	}

	public Page cast() {
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
	
	public String toString(){
		return new StringBuilder().append(getClass().getSimpleName())
			.append("@").append(Integer.toHexString(System.identityHashCode(this)))
			.append("[").append(page.toString()).append("]")
			.toString();
	}
}
