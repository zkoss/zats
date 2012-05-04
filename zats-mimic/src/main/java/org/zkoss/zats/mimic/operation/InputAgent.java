/* InputAgent.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for inputting value into component.
 * According the type of component, the agent will perform different actual operations.
 * @author pao
 */
public interface InputAgent extends OperationAgent {
	
	/**
	 * To input value into component.
	 */
	void input(Object value);
}
