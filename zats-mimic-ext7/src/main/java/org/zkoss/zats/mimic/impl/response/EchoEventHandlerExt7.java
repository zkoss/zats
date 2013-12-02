/* EchoEventHandlerExt7.java

	Purpose:
		
	Description:
		
	History:
		Apr 25, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import org.zkoss.zats.common.json.JSONObject;
import org.zkoss.zats.common.json.JSONValue;

/**
 * The response handler for echo event in ZK 7.
 * @author pao
 */
public class EchoEventHandlerExt7 extends EchoEventHandler {

	@Override
	protected String parseUuid(Object object) {
		JSONObject json = (JSONObject) JSONValue.parse(object.toString());
		return json.get("$u").toString();
	}
}
