/* GroupAgent.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for group changing operation.
 * @author pao
 */
public interface GroupAgent extends OperationAgent {
	/**
	 * To group this column.
	 */
	void group();
}
