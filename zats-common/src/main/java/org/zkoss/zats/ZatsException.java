/* ConversationException.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats;
/**
 * the exception represents a zats system error
 * @author dennis
 *
 */
public class ZatsException extends RuntimeException {
	private static final long serialVersionUID = -307047315005722126L;

	public ZatsException(String message) {
		super(message);
	}

	public ZatsException(String message, Throwable cause) {
		super(message, cause);
	}
}
