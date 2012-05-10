/* HoverAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/5/2 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To hover a mouse pointer over a component that extends from HtmlBasedComponent. 
 * Most of ZK components extend HtmlBasedComponent..
 * 
 * @author Hawk
 *
 */
public interface HoverAgent extends OperationAgent{
	

	/**
	 * Move mouse over a component.
	 */
	public void moveOver();
	
	/**
	 * Move mouse out of a component.
	 */
	public void moveOut();
}
