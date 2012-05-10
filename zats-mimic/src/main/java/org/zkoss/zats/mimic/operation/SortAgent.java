/* SortAgent.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent for sorting operation. Supported components are Column, Treecol,and ListHeader.
 * @author pao
 */
public interface SortAgent extends OperationAgent {

	/**
	 * To sort a header with specified sorting order.
	 * @param ascending true indicates ascending; false indicates descending.
	 */
	void sort(boolean ascending);
}
