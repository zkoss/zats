/* Agent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

/**
 * A interface represented a agent for a agent from ZUML structure. e.x , the Desktop , Page or Component
 * 
 * @author pao
 */
public interface Agent {


	/**
	 * get conversation this agent belonged to.
	 * 
	 * @return conversation
	 */
	Conversation getConversation();
	
	Object getDelegatee();
}
