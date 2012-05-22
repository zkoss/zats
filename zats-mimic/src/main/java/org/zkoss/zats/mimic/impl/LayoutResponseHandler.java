/* LayoutResponseHandler.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

/**
 * The interface of layout response handler.
 * The handler can process response when client opened a URL,
 * and modify status of client if necessary. 
 * @author pao
 */
public interface LayoutResponseHandler {
	
	/**
	 * perform layout response processing.
	 * @param controller client controller
	 * @param response RAW text of layout response (HTML/XHTML)
	 */
	void process(ClientCtrl controller , String response);
}
