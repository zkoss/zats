/* SelectByIndexAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 10, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To perform a selection by specific index on a component.
 * @author pao
 */
public interface SelectByIndexAgent extends OperationAgent {
	
	/**
	 * select item by specific index. 
	 */
	void select(int index);
}
