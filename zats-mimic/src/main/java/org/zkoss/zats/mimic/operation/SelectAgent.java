/* SelectAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * to do single selection on a component
 * @author dennis
 *
 */
public interface SelectAgent extends OperationAgent {

	public void select(int index);
}
