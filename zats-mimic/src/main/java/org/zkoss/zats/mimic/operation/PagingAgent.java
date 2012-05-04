/* PagingAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * @author Hawk
 *
 */
public interface PagingAgent extends OperationAgent{

	public void goTo(int page);
}
