/* PageAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;

import org.zkoss.zk.ui.Page;

public interface PageAgent extends Agent {
	/**
	 * get ID. of the page.
	 * 
	 * @return ID or null if it hasn't.
	 */
	String getId();
	
	/**
	 * get UUID. of this the page.
	 * 
	 * @return UUID.
	 */
	String getUuid();

	/**
	 * 
	 * @return
	 */
	List<ComponentAgent> getRoots();
	
	/**
	 * get attribute by specify name.
	 * 
	 * @param name
	 *            attribute name.
	 * @return attribute value or null if not found or otherwise.
	 */
	Object getAttribute(String name);

	/**
	 * get desktop of this page
	 */
	DesktopAgent getDesktop();

	/**
	 * get the native Page.
	 * 
	 * @return page
	 */
	Page getPage();
	
	/**
	 * to find the first component agent with the selector in this page
	 * @param selector the selector
	 * @return the first component agent, null if not found
	 */
	ComponentAgent query(String selector);
	
	/**
	 * to find the component agents with the selector in this page
	 * @param selector the selector
	 * @return the component agents
	 */
	List<ComponentAgent> queryAll(String selector);
}
