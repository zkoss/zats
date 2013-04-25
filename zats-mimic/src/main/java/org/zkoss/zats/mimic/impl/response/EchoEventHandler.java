/* EchoEventHandler.java

	Purpose:
		
	Description:
		
	History:
		Apr 24, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.common.json.JSONObject;
import org.zkoss.zats.common.json.JSONValue;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.EchoEventMode;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.UpdateResponseHandler;
import org.zkoss.zats.mimic.impl.au.AuUtility;
import org.zkoss.zk.au.AuResponse;

/**
 * The response handler for echo event.
 * @author pao
 */
public class EchoEventHandler implements UpdateResponseHandler {
	private final static String REQUEST_ECHO_EVENT = "echo";
	private final static String RESPONSE_ECHO_EVENT = "echo2";

	public void process(DesktopAgent desktop, Map<String, Object> jsonObject) {
		// fetch all commands and find echo event
		List<AuResponse> responses = AuUtility.convertToResponses(jsonObject);
		for (AuResponse resp : responses) {
			if (RESPONSE_ECHO_EVENT.equals(resp.getCommand())) {

				// fetch data and post a piggyback event
				Object[] rawData = resp.getRawData();
				String uuid = getUuid(rawData[0].toString());
				Object[] replyData = new Object[rawData.length - 1];
				System.arraycopy(rawData, 1, replyData, 0, rawData.length - 1);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("", replyData);
				Client client = desktop.getClient();
				if (client.getEchoEventMode() == EchoEventMode.PIGGYBACK) { // check echo event mode
					((ClientCtrl) client).postPiggyback(desktop.getId(), uuid, REQUEST_ECHO_EVENT, data, true);
				} else {
					// #ZATS-11: note that if it posts a non-piggyback event, the client will posting events continually
					((ClientCtrl) client).postUpdate(desktop.getId(), uuid, REQUEST_ECHO_EVENT, data, true);
				}
			}
		}
	}

	/**
	 * UUID format in ZK 5: "UUID"
	 * UUID format in ZK 6: {$u:'UUID'}
	 * i.e. it's a JSON object in ZK 6.  
	 */
	private String getUuid(String raw) {
		Object json = JSONValue.parse(raw);
		if (json instanceof JSONObject) {
			return ((JSONObject) json).get("$u").toString();
		} else {
			return json.toString();
		}
	}
}
