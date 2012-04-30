/* MaximizeAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To maximize or minimize size of a component.
 * @author pao
 */
public interface MaximizeAgent extends OperationAgent {
	/**
	 * To maximized size of a component.
	 * If the component isn't maximizable, it will throw exception. 
	 * @param maximized true indicated maximization.
	 */
	void setMaximized(boolean maximized);

	/**
	 * To minimized size of a component.
	 * If the component isn't minimizable, it will throw exception.
	 * @param minimized true indicated minimization.
	 */
	void setMinimized(boolean minimized);
}
