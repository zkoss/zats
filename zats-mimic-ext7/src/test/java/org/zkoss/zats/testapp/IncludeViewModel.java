/* IncludeViewModel.java

	Purpose:
		
	Description:
		
	History:
		Jul 3, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testapp;

/**
 * for include-ext7.zul
 * @author pao
 */
public class IncludeViewModel {

	private String message;

	public IncludeViewModel() {
		message = "Hello! ZK!";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
