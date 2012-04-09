/* CloseAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/4/6 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * When closing a closable component, you cannot re-open it. Because it's detached from desktop.
 * Handle the onClose of component.
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
