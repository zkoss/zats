/* SelectAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * To do selection on a component.
 * According multiple selection status of parent component (such as listbox),
 * this selection might be performed at multiple selection mode.
 * @author dennis
 */
public interface SelectAgent extends OperationAgent {

	/**
	 * to select a item.
	 */
	public void select();

	/**
	 * to deselect a item.
	 * If the parent component isn't at multiple selection mode, it will throw exception. 
	 */
	public void deselect();
}
