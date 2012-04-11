/* Agent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

/**
 * A interface represented a agent for a element from ZUML structure. e.x , the Desktop , Page or Component
 * 
 * @author pao
 * @author Dennis
 */
public interface Agent {


	/**
	 * get client this agent belonged to.
	 */
	Client getClient();
	
	/**
	 * get the delegatee object
	 */
	Object getDelegatee();
}
