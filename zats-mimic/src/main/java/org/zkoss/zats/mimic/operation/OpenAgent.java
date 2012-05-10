/* OpenAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent to mimic open operation to a component. Supported components are Combobutton, Popup, Bandbox, 
 * Combobox, Groupbox, Panel,Window,Detail, Group, Listgroup, Treeitem, Center, East, North, South, and West.
 * 
 * @author dennis
 *
 */
public interface OpenAgent extends OperationAgent{

	/**
	 * open a component
	 * @param open true for open
	 */
	void open(boolean open);
}
