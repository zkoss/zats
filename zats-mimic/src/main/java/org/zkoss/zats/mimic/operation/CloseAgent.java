/* CloseAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/4/6 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To close a component including Window, Panel, and Tab.
 * When closing a closable component, it's detached from desktop by default behavior.
 * 
 * 
 * @author Hawk
 *
 */
public interface CloseAgent extends OperationAgent{

	/**
	 * close the component
	 */
	void close();
}
