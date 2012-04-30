/* TypeAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * The type operation.
 * @author pao
 */
public interface TypeAgent extends OperationAgent {

	/**
	 * To type data into a component. The value should be valid for target
	 * component. e.g. Intbox only accepts integer. If the target is Datebox or Timebox,
	 * the format depends on its "format" attribute. 
	 * @param value the input value
	 */
	public void type(String value);

	/**
	 * To simulate typing data into a component and it will send onChanging event to server.
	 * @param value the typing value.
	 */
	public void typing(String value);
}
