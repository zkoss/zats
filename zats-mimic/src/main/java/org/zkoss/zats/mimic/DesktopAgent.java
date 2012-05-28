/* DesktopAgent.java

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
 * The desktop agent, represents a server-side ZK desktop
 * 
 * @author pao
 * @author Dennis
 */
public interface DesktopAgent extends Agent {
	/**
	 * get ID. of this the desktop.
	 * 
	 * @return ID or null if it hasn't.
	 */
	String getId();
	
	
	/**
	 * get pages in this desktop
	 */
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
	 * to find the first component agent with the selector
	 * @param selector the selector
	 * @return the first component agent, null if not found
	 */
	ComponentAgent query(String selector);
	
	/**
	 * to find the component agents with the selector
	 * @param selector the selector
	 * @return the component agents
	 */
	List<ComponentAgent> queryAll(String selector);

	
	/**
	 * Try to get a instance of target class for this desktop agent, the target class
	 * is usually a {@link OperationAgent} or a native {@link Component} <br/>
	 * 
	 * if it cannot get a instance of target class, it will throw
	 * {@link AgentException}.
	 * 
	 * @param clazz
	 *            class of specify operation.
	 * @return operation object.
	 */
	<T> T as(Class<T> clazz);
	
	/**
	 * Can get a instance of target class for this desktop
	 * 
	 * @param clazz
	 *            the class cast to.
	 * @return true if can get a instance of target class
	 */
	<T> boolean is(Class<T> clazz);
	
	/**
	 * destroy this desktop.
	 */
	void destroy();
	
	/**
	 * get the downloadable file currently.
	 * @return downloadable file or null if there is no downloadable file currently.
	 */
	Downloadable getDownloadable();
}
