/* TypeAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * The operation for typing.
 * 
 * @author pao
 */
public interface TypeAgent extends OperationAgent {

	/**
	 * To type data into component. The value should be valid for target
	 * component. e.g Intbox is only accepted integer. If the target is Datebox,
	 * the format should be "yyyy/MM/dd" or "yyyy/MM/dd HH:mm:ss". If the target
	 * is Timebox, the format shoudl be "HH:mm:ss".
	 * 
	 * @param value
	 *            the input value
	 * @return self
	 */
	public void type(String value);
}
