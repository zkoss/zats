/* OpenAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * Handle the onOpen of component
 * @author dennis
 *
 */
public interface OpenAgent extends OperationAgent{

	/**
	 * open the component
	 * @param open true for open
	 */
	void open(boolean open);
}
