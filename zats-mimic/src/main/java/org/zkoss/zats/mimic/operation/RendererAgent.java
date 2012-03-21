/* RendererAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * To render sub-items (the sub-item is the component that renders by a component renderer)
 * of a component that is not rendered yet. <p/>
 * You have to use this operation on the component that supports model, for example the listitem of listbox, 
 * to prevent that sub-items are not rendered yet.<p/>
 * 
 * @author dennis
 *
 */
public interface RendererAgent extends OperationAgent{

	/**
	 * render the sub-item from index x to y. both x and y are included.
	 * @param x the start index(zero-base, included) of sub-item to render, -1 means from 0 
	 * @param t the end index(zero-base, , included) of sub-item to render, -1 means to end of the sub-items
	 */
	public void render(int x, int y);
	
}
