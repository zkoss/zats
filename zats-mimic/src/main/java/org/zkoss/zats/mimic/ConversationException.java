/* ConversationException.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

public class ConversationException extends RuntimeException {
	private static final long serialVersionUID = -307047315005722126L;

	public ConversationException(String message) {
		super(message);
	}

	public ConversationException(String message, Throwable cause) {
		super(message, cause);
	}
}
