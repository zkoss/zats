/* InputAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

/**
 * To perform input to a component that extends from  InputElement.
 * For most input components that can type text in, use {@link #type(String)} and for others use {@link #input(Object)}. 
 * @author pao
 * @author dennis
 */

public interface InputAgent extends OperationAgent {

	
	/**
	 * To input value into a component. This method generalizes various input operation to a InputElement, e.g. typing in a Textbox,
	 * or scrolling a Slider. For those components that you cannot type text in, e.g. Slider is inputed by scrolling its bar.
	 *  you can use this method to mimic the special input operation other than typing. <br/>
	 * You can pass in an Integer for a a intbox, Double for a doublebox, and Date for a datebox. 
	 * Parameter's type depends on target component.
	 * @param value the input value
	 */
	void input(Object value);
	
	/**
	 * To type text into a component. The value should be valid for target component, e.g. Intbox only accepts integer. 
	 * If the target is Datebox or Timebox, your input text format should match "format" attribute's pattern. 
	 * @param value the input value
	 */
	public void type(String value);

	/**
	 * To simulate typing data into a component and it will send onChanging event to server.
	 * @param value the typing value.
	 */
	public void typing(String value);
	
	/**
	 * To select (highlight) a range to text of an InputElement by specifying 2 indexes.
	 * @param start the beginning index,start from 0, inclusive.
	 * @param end the ending index, exclusive.
	 */
	public void select(int start, int end);
}
