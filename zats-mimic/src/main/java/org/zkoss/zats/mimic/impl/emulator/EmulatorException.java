/* EmulatorException.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.emulator;

public class EmulatorException extends RuntimeException {
	private static final long serialVersionUID = 2958409858718318588L;

	public EmulatorException(String message) {
		super(message);
	}

	public EmulatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
