/* PagingAgent.java

	Purpose:
		
	Description:
		
	History:

		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for switching page operation.
 * @author pao
 */
public interface PagingAgent extends OperationAgent {

	/**
	 * Move to specified page.
	 * @param pageIndex index of page, starts from 0 (1st page)
	 */
	public void moveTo(int pageIndex);
}
