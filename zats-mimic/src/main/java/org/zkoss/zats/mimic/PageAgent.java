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
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
/**
 * The page agent, represents a server-side zk page
 * 
 * @author pao
 * @author Dennis
 */
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
	 * try to transfer the page agent to the target class, the target class
	 * is usually a {@link OperationAgent} or a native {@link Page} <br/>
	 * 
	 * if it cannot transfer to target class, it will throw
	 * {@link AgentException}.
	 * 
	 * @param clazz
	 *            class of specify operation.
	 * @return operation object.
	 */
	<T> T as(Class<T> clazz);

	/**
	 * get desktop of this page
	 */
	DesktopAgent getDesktop();
	
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
