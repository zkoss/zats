/* EchoEventHandlerExt96.java

	Purpose:
		
	Description:
		
	History:
		Fri Jun 4 12:46:50 CST 2021, Created by jameschu

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import org.zkoss.zats.common.json.JSONObject;
import org.zkoss.zats.common.json.JSONValue;

/**
 * The response handler for echo event in ZK 9.6
 * @author pao
 */
public class EchoEventHandlerExt96 extends EchoEventHandler {

	@Override
	protected String parseUuid(Object object) {
		JSONObject json = (JSONObject) JSONValue.parse(object.toString());
		return json.get("$u").toString();
	}
}
