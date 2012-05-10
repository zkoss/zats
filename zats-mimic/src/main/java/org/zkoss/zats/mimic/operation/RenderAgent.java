/* RenderAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * <p>
 * To render a child components of a data component that is not rendered yet. 
 * Listbox and Grid supports this operation.</p>
 * <p>
 * Because of ZK will not render all sub-items at first,  it just pre-loads first few items. 
 * Until a user scrolls the scroll bar down, it loads and renders subsequent items.
 * You will retrieve an invalid child component before it's rendered. Hence if you want to manipulate a not-rendered child component, 
 * you have to render it first.</p>
 * 
 * @author dennis
 *
 */
public interface RenderAgent extends OperationAgent{

	/**
	 * Render the sub-item from index "start" to "end".
	 * @param start the start index(zero-base, included) of sub-item to render, -1 means from 0 
	 * @param end the end index(zero-base, exclusive) of sub-item to render, -1 means to end of the sub-items
	 */
	public void render(int start, int end);
	
}
