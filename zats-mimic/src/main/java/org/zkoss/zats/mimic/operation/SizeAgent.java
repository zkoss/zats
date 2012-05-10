/* SizeAgent.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * <p>
 * The agent of sizing operation. 
 * Only Window and Panel support this operation.
 * </p>
 * 
 * @author pao
 */
public interface SizeAgent extends OperationAgent {
	/**
	 * To maximize a component's size. 
	 * If the component isn't maximizable, it will throw exception. 
	 * @param maximized true indicated maximization.
	 */
	void maximize(boolean maximized);

	/**
	 * To minimized component's size.
	 * If the component isn't minimizable, it will throw exception.
	 * @param minimized true indicated minimization.
	 */
	void minimize(boolean minimized);
	
	/**
	 * To resize a component.
	 * If a components doesn't set its width or height, we will set width to 200px and height to 100px.
	 * @param width new width or specify -1 if no change.
	 * @param height new height or specify -1 if no change.
	 */
	void resize(int width , int height);
}
