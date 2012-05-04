/* MoveAgent.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for moving operation.
 * @author pao
 */
public interface MoveAgent {

	/**
	 * To move a component.
	 * @param left distance from left.
	 * @param top distance from top.
	 */
	void move(int left, int top);
}
