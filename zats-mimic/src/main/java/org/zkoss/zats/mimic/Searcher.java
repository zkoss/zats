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
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.zkoss.Version;
import org.zkoss.zats.common.select.Selectors;
import org.zkoss.zats.mimic.impl.DefaultComponentAgent;
import org.zkoss.zats.mimic.impl.DefaultPageAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;

/**
 * A tool for searching components.
 * 
 * @author pao
 */
public class Searcher {
	private static Logger logger = Logger.getLogger(Searcher.class.getName());

	/**
	 * find components matched specify selector.
	 * 
	 * @param parent the reference agent for selector
	 * @param selector the selector string.
	 * @return a list contained matched components.
	 */
	@SuppressWarnings("unchecked")
	public static List<ComponentAgent> findAll(Agent parent, String selector) {
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
			List<Component> list;
			
			Object base = parent.getDelegatee();
			
			if (base instanceof Desktop) {
				Collection<Page> pages = ((Desktop)base).getPages();
				list = new ArrayList<Component>();
				for(Page p : pages){
					List<Component> pl = (List<Component>) findByPage.invoke(null, p , selector);
					if(pl!=null && pl.size()>0){
						list.addAll(pl);
					}
				}
			} else if (base instanceof Page) {
				list = (List<Component>) findByPage.invoke(null,(Page)base, selector);
			} else if (base instanceof Component){
				Method findByComp = selectors.getMethod("find",
						Component.class, String.class);
				list = (List<Component>) findByComp.invoke(null, (Component)base, selector);
			} else{
				throw new ConversationException("unsupported type "+base);
			}
			
			List<ComponentAgent> agents = new ArrayList<ComponentAgent>(list.size());
			
			PageAgent lastPg = null;
			
			for (Component comp : list){
				Page pg = comp.getPage();
				if(lastPg==null || !lastPg.getDelegatee().equals(pg)){
					lastPg = new DefaultPageAgent(parent.getConversation().getDesktop(),pg);
				}
				agents.add(new DefaultComponentAgent(lastPg, comp));
			}
			return agents;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * find first component matched specify selector.
	 * @param parent the reference agent for selector
	 * @param selector the selector string.
	 * @return matched component or null if not found.
	 */
	public static ComponentAgent find(Agent parent, String selector) {
		List<ComponentAgent> agents = findAll(parent, selector);
		return agents.size() > 0 ? agents.get(0) : null;
	}

	public static ComponentAgent find(String selector) {
		return Searcher.find(Conversations.getDesktop(), selector);
	}

	public static List<ComponentAgent> findAll(String selector) {
		return Searcher.findAll(Conversations.getDesktop(), selector);
	}
}
