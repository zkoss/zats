/* QueryAgent.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2014 Created by henrichen

Copyright (C) 2014 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic;

import java.util.List;

import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.Component;

/**
 * Agent that can be used to query other agent. 
 * @author henrichen
 * @since 1.2.1
 */
public interface QueryAgent extends Agent {
	/**
	 * to find the first component agent with the selector in this query agent.
	 * @param selector the selector
	 * @return the first component agent, null if not found
	 */
	ComponentAgent query(String selector);
	
	/**
	 * to find the component agents with the selector in this query agent.
	 * @param selector the selector
	 * @return the component agents
	 */
	List<ComponentAgent> queryAll(String selector);

	/**
	 * Try to get a instance of target class for this query agent, the target class
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
	 * Can get a instance of target class for this query agent.
	 * 
	 * @param clazz
	 *            the class cast to.
	 * @return true if can get a instance of target class
	 */
	<T> boolean is(Class<T> clazz);
}
