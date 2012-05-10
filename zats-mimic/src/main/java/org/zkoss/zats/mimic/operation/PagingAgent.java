/* PagingAgent.java

	Purpose:
		
	Description:
		
	History:

		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for switching page operation. Only Paging supports this operation. 
 * When Listbox, Grid, and Tree are in "pagin" mold, they all have an auto-created, internal Paging component.
 * You can retrieve them by Selector syntax.  
 * @author pao
 */
public interface PagingAgent extends OperationAgent {

	/**
	 * Move to specified page index.
	 * @param pageIndex index of page, starts from 0 (1st page)
	 */
	public void moveTo(int pageIndex);
}
