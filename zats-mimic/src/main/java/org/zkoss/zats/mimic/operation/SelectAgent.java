/* SelectAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * To select a single item of a data component. You should use this operation on a child component 
 * including Comboitem, Tab, Listitem, and Treeitem.
 * @author dennis
 */
public interface SelectAgent extends OperationAgent {

	/**
	 * to select a item.
	 */
	public void select();
}
