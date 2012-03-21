/* PageAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;
import java.util.Map;

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

	List<ComponentAgent> getChildren();
	
	/**
	 * get attribute by specify name.
	 * 
	 * @param name
	 *            attribute name.
	 * @return attribute value or null if not found or otherwise.
	 */
	Object getAttribute(String name);

	/**
	 * get all attributes of the page.
	 * 
	 * @return a map of attributes.
	 */
	Map<String, Object> getAttributes();

	DesktopAgent getDesktop();

	/**
	 * get the native Page.
	 * 
	 * @return page
	 */
	Page getPage();
}
