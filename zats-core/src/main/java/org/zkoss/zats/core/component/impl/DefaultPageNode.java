package org.zkoss.zats.core.component.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.PageNode;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

public class DefaultPageNode implements PageNode
{
	private DesktopNode desktopNode;
	private Page page;

	public DefaultPageNode(DesktopNode desktopNode, Page page)
	{
		this.desktopNode = desktopNode;
		this.page = page;
	}

	public String getId()
	{
		return page.getId();
	}

	public String getType()
	{
		return "page";
	}

	public Object getAttribute(String name)
	{
		return page.getAttribute(name);
	}

	public Map<String, Object> getAttributes()
	{
		return page.getAttributes();
	}

	public String getUuid()
	{
		return page.getUuid();
	}

	public List<ComponentNode> getChildren()
	{
		List<ComponentNode> nodes = new ArrayList<ComponentNode>();
		Iterator<?> iterator = page.getRoots().iterator();
		while(iterator.hasNext())
			nodes.add(new DefaultComponentNode(this, (Component)iterator.next()));
		return nodes;
	}

	public Conversation getConversation()
	{
		return desktopNode.getConversation();
	}

	public DesktopNode getDesktop()
	{
		return desktopNode;
	}

	public Page cast()
	{
		return page;
	}
}
