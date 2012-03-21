/* Searcher.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.zkoss.Version;
import org.zkoss.zats.common.select.Selectors;
import org.zkoss.zats.mimic.impl.node.DefaultComponentAgent;
import org.zkoss.zats.mimic.impl.node.DefaultPageAgent;
import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.node.DesktopAgent;
import org.zkoss.zats.mimic.node.Agent;
import org.zkoss.zats.mimic.node.PageAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

/**
 * A tool for searching components.
 * 
 * @author pao
 */
public class Searcher {
	private static Logger logger = Logger.getLogger(Searcher.class.getName());

	/**
	 * find components matched specify selector. notice: if refer the Desktop,
	 * it will only find first page.
	 * 
	 * @param root
	 *            the reference node for selector (it could be Desktop or Page).
	 * @param selector
	 *            the selector string.
	 * @return a list contained matched components.
	 */
	@SuppressWarnings("unchecked")
	public static List<ComponentAgent> findAll(Agent root, String selector) {
		try {
			// detect zk version to choose implementation
			Class<?> selectors;

			try {
				selectors = Class.forName("org.zkoss.zk.ui.select.Selectors");
				logger.finest("using zk6 selectors: "
						+ Version.class.getField("UID").get(null));
			} catch (ClassNotFoundException e) {
				selectors = Selectors.class;
				logger.finest("using zats selectors: "
						+ Version.class.getField("UID").get(null));
			}
			Method findByPage = selectors.getMethod("find", Page.class,
					String.class);

			// find components
			PageAgent pageNode;
			List<Component> list;
			if (root instanceof DesktopAgent) {
				DesktopAgent desktopNode = (DesktopAgent) root;
				// TODO should we check all the pages?
				pageNode = new DefaultPageAgent(desktopNode, desktopNode.cast()
						.getFirstPage());
				list = (List<Component>) findByPage.invoke(null,
						pageNode.cast(), selector);
			} else if (root instanceof PageAgent) {
				pageNode = (PageAgent) root;
				list = (List<Component>) findByPage.invoke(null,
						pageNode.cast(), selector);
			} else {
				ComponentAgent compNode = (ComponentAgent) root;
				pageNode = compNode.getPage();
				Method findByComp = selectors.getMethod("find",
						Component.class, String.class);
				list = (List<Component>) findByComp.invoke(null,
						compNode.as(Component.class), selector);
			}
			List<ComponentAgent> nodes = new ArrayList<ComponentAgent>(
					list.size());
			for (Component comp : list)
				nodes.add(new DefaultComponentAgent(pageNode, comp));
			return nodes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * find first component matched specify selector. notice: if refer the
	 * Desktop, it will only find first page.
	 * 
	 * @param root
	 *            the reference node for selector (it could be Desktop or Page).
	 * @param selector
	 *            the selector string.
	 * @return matched component or null if not found.
	 */
	public static ComponentAgent find(Agent root, String selector) {
		List<ComponentAgent> nodes = findAll(root, selector);
		return nodes.size() > 0 ? nodes.get(0) : null;
	}

	public static ComponentAgent find(String selector) {
		return Searcher.find(Conversations.getDesktop(), selector);
	}

	public static List<ComponentAgent> findAll(String selector) {
		return Searcher.findAll(Conversations.getDesktop(), selector);
	}
}
