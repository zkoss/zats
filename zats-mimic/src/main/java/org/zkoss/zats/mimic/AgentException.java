/* AgentException.java

	Purpose:
		
	Description:
		
	History:
		2012/3/21 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic;

/**
 * an exception be threw by a Agent, it is usually represent a load or ajax error 
 * @author Dennis
 *
 */
public class AgentException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AgentException() {
		super();
	}

	public AgentException(String message, Throwable cause) {
		super(message, cause);
	}

	public AgentException(String message) {
		super(message);
	}

	public AgentException(Throwable cause) {
		super(cause);
	}

}
