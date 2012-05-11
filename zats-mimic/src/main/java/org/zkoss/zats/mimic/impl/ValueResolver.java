/* ValueResolver.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.zats.mimic.Agent;


/**
 * ValueResolver will try to convert a ComponentAgent to the object of specified class. 
 * If failed, it will throw run-time exception. 
 * A ComponentAgent can be converted into multiple types of objects, e.g. OperationAgent or native ZK component class.
 * We design the value resolver mechanism to keep extension for potential conversion in the future.
 *  
 * @author dennis
 *
 */
public interface ValueResolver {
	/**
	 * resolve the component agent to a object by registered value resolver
	 */
	<T> T resolve(Agent agent,Class<T> clazz);
}
