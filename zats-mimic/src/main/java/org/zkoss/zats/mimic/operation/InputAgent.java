/* InputAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * To operate user input.
 * You can input data either by {@link #type(String)} or {@link #input(Object)}
 * @author pao
 * @author dennis
 */

public interface InputAgent extends OperationAgent {

	
	/**
	 * To input value into component. 
	 * You can use the Integer or Double directly for a intbox or doublebox, use Date for a datebox.
	 * @param value the input value
	 */
	void input(Object value);
	
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
	
	/**
	 * To select (highlight) a range to text of an InputElement by specifying 2 indexes. Index value begins from 0.
	 * @param start the beginning index, inclusive.
	 * @param end the ending index, exclusive.
	 */
	public void select(int start, int end);
}
