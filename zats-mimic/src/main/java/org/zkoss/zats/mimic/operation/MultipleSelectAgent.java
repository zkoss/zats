/* MultipleSelectAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 6, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To do multiple selection on a component.
 * @author pao
 */
public interface MultipleSelectAgent extends OperationAgent {

	/**
	 * to select an item.
	 * If the parent component isn't at multiple selection mode, it will throw exception. 
	 */
	public void select();

	/**
	 * to de-select an item.
	 * If the parent component isn't at multiple selection mode, it will throw exception. 
	 */
	public void deselect();

}
