/* TypingAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 27, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The typing operation.
 * @author pao
 */
public interface TypingAgent extends OperationAgent {
	
	/**
	 * To simulate typing data into a component and it will send onChanging event to server.
	 * @param value the typing value.
	 */
	public void typing(String value);
}
