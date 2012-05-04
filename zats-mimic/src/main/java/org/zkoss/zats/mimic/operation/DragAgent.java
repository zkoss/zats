/* DragAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/5/2 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

import org.zkoss.zats.mimic.ComponentAgent;

/**
 * @author Hawk
 *
 */
public interface DragAgent extends OperationAgent{

	/**
	 * drag and drop a component
	 * @param target the destination component which you want to drop on
	 */
	public void dropOn(ComponentAgent target);
}
