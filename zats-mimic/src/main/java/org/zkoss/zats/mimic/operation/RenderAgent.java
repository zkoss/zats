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
 * To render sub-items of a component that is not rendered yet. The sub-item is the component that is rendered by a component renderer. </p>
 * <p>
 * You have to render child components of component that supports model first, for example the listitem of listbox, 
 * to prevent that sub-items are not rendered yet. You will get an invalid child component if it's not rendered.</p>
 * 
 * @author dennis
 *
 */
public interface RenderAgent extends OperationAgent{

	/**
	 * render the sub-item from index "start" to "end". both "start" and "end" are included.
	 * @param start the start index(zero-base, included) of sub-item to render, -1 means from 0 
	 * @param end the end index(zero-base, exclusive) of sub-item to render, -1 means to end of the sub-items
	 */
	public void render(int start, int end);
	
}
