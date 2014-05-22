/* PageAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;

import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.Component;
/**
 * The page agent, represents a server-side zk page
 * 
 * @author pao
 * @author Dennis
 * @author henrichen
 */
public interface PageAgent extends QueryAgent {
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
	 * get root component agents at the page.
	 * @return a list contained root component agents.
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
}
