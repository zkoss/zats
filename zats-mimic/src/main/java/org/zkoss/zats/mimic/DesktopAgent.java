/* DesktopAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Desktop;

public interface DesktopAgent extends Agent {
	/**
	 * get ID. of this the desktop.
	 * 
	 * @return ID or null if it hasn't.
	 */
	String getId();
	
	List<PageAgent> getPages();
	
	
	/**
	 * get attribute by specify name.
	 * 
	 * @param name
	 *            attribute name.
	 * @return attribute value or null if not found or otherwise.
	 */
	Object getAttribute(String name);

	/**
	 * get all attributes of the desktop.
	 * 
	 * @return a map of attributes.
	 */
	Map<String, Object> getAttributes();

	/**
	 * get the native Desktop.
	 * 
	 * @return desktop
	 */
	Desktop getDesktop();
}
