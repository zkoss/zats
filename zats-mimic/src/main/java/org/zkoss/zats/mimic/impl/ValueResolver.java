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
 * To resolve component agent to a special object
 * @author dennis
 *
 */
public interface ValueResolver {
	/**
	 * resolve the component agent to a object by registered value resolver
	 */
	<T> T resolve(Agent agent,Class<T> clazz);
}
