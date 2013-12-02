/* Searcher.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.zkoss.Version;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.common.select.Selectors;
import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.PageAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;

/**
 * A tool for searching components with Selector syntax.
 * 
 * @author pao
 */
/*public*/ class Searcher {
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
			List<Component> foundComponents;
			
			Object base = parent.getDelegatee();
			
			if (base instanceof Desktop) {
				Collection<Page> pages = ((Desktop)base).getPages();
				foundComponents = new ArrayList<Component>();
				for(Page p : pages){
					
					// ZATS-20: check page is empty or not before searching by selector
					if(p.getFirstRoot() == null) {
						continue;
					}
					
					List<Component> pl = (List<Component>) findByPage.invoke(null, p , selector);
					if(pl!=null && pl.size()>0){
						foundComponents.addAll(pl);
					}
				}
			} else if (base instanceof Page) {
				Page p = (Page) base;

				// ZATS-20: check page is empty or not before searching by selector
				if (p.getFirstRoot() == null) {
					foundComponents = new LinkedList<Component>();
				} else {
					foundComponents = (List<Component>) findByPage.invoke(null, p, selector);
				}

			} else if (base instanceof Component){
				Method findByComp = selectors.getMethod("find",
						Component.class, String.class);
				foundComponents = (List<Component>) findByComp.invoke(null, (Component)base, selector);
			} else{
				throw new ZatsException("unsupported type "+base);
			}
			
			List<ComponentAgent> foundAgents = new ArrayList<ComponentAgent>(foundComponents.size());
			
			PageAgent lastPage = null;
			DesktopAgent desktopAgent = null;
			//wrap components as ComponentAgent
			for (Component comp : foundComponents){
				Page pg = comp.getPage();
				if (desktopAgent == null) {
					desktopAgent = new DefaultDesktopAgent(parent.getClient(), pg.getDesktop());
				}
				//components might come from different pages
				if(lastPage==null || !lastPage.getDelegatee().equals(pg)){
					lastPage = new DefaultPageAgent(desktopAgent,pg);
				}
				foundAgents.add(new DefaultComponentAgent(lastPage, comp));
			}
			return foundAgents;
		} catch (Exception e) {
			throw new ZatsException(e.getMessage(),e);
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
}
