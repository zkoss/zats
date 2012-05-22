/* UpdateResponseHandler.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.Map;

/**
 * The interface of asynchronous update response handler.
 * The handler can process response when client post an AU event,
 * and modify status of desktop if necessary. 
 * @author pao
 */
public interface UpdateResponseHandler {

	/**
	 * perform AU response processing.
	 * @param controller client controller
	 * @param response response data
	 */
	void process(DesktopCtrl controller , Map<String, Object> jsonObject);
}
