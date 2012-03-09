package org.zkoss.zats.core;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.Node;
import org.zkoss.zats.core.component.PageNode;
import org.zkoss.zats.core.component.impl.DefaultComponentNode;
import org.zkoss.zats.core.component.impl.DefaultPageNode;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

/**
 * A tool for searching components.
 * @author pao
 */
public class Searcher
{
	/**
	 * find components matched specify selector.
	 * notice: if refer the Desktop, it will only find first page.
	 * @param root the reference node for selector (it could be Desktop or Page).
	 * @param selector the selector string.
	 * @return a list contained matched components.
	 */
	public static List<ComponentNode> findAll(Node root, String selector)
	{
		try
		{
			Class.forName("org.zkoss.zk.ui.select.Selectors");

		}
		catch(ClassNotFoundException e)
		{
			// TODO implemention for zk5
			throw new UnsupportedOperationException(e);
		}
		// TODO implement through Selector in ZK 6
		PageNode pageNode;
		List<Component> list;
		if(root instanceof DesktopNode)
		{
			DesktopNode desktopNode = (DesktopNode)root;
			pageNode = new DefaultPageNode(desktopNode, desktopNode.cast().getFirstPage());
			list = Selectors.find(pageNode.cast(), selector);
		}
		else if(root instanceof PageNode)
		{
			pageNode = (PageNode)root;
			list = Selectors.find(pageNode.cast(), selector);
		}
		else
		{
			ComponentNode compNode = (ComponentNode)root;
			pageNode = compNode.getPage();
			list = Selectors.find(compNode.cast(Component.class), selector);
		}
		List<ComponentNode> nodes = new ArrayList<ComponentNode>(list.size());
		for(Component comp : list)
			nodes.add(new DefaultComponentNode(pageNode, comp));
		return nodes;
	}

	/**
	 * find first component matched specify selector.
	 * notice: if refer the Desktop, it will only find first page.
	 * @param root the reference node for selector (it could be Desktop or Page).
	 * @param selector the selector string.
	 * @return matched component or null if not found.
	 */
	public static ComponentNode find(Node root, String selector)
	{
		List<ComponentNode> nodes = findAll(root, selector);
		return nodes.size() > 0 ? nodes.get(0) : null;
	}

	public static ComponentNode find(String selector)
	{
		return Searcher.find(Conversations.getDesktop(), selector);
	}
}
