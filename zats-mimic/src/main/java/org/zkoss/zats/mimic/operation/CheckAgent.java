/* CheckAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * To change the check status of components.
 * @author pao
 */
public interface CheckAgent extends OperationAgent {
	
	/**
	 * change the check status.
	 */
	void check(boolean checked);
}
