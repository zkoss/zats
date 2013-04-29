/* EchoEventHandler.java

	Purpose:
		
	Description:
		
	History:
		Apr 24, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.EchoEventMode;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.LayoutResponseHandler;
import org.zkoss.zats.mimic.impl.UpdateResponseHandler;
import org.zkoss.zats.mimic.impl.au.AuUtility;
import org.zkoss.zk.au.AuResponse;

/**
 * The response handler for echo event.
 * @author pao
 */
public class EchoEventHandler implements UpdateResponseHandler, LayoutResponseHandler {
	public final static String REGISTER_KEY = "echo";
	private final static String REQUEST_ECHO_EVENT = "echo";
	private final static String RESPONSE_ECHO_EVENT = "echo2";

	public void process(DesktopAgent desktop, Map<String, Object> jsonObject) {
		// fetch all commands and find echo event
		List<AuResponse> responses = AuUtility.convertToResponses(jsonObject);
		for (AuResponse resp : responses) {
			if (RESPONSE_ECHO_EVENT.equals(resp.getCommand())) {

				// fetch data and post a piggyback event
				Object[] rawData = resp.getRawData();
				String uuid = parseUuid(rawData[0]);
				List<Object> replyData = new ArrayList<Object>();
				for (int i = 1; i < rawData.length; ++i) {
					replyData.add(rawData[i]);
				}
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

	protected String parseUuid(Object object) {
		return object.toString();
	}

	public void process(DesktopAgent desktopAgent, String response) {
		// ZATS-11: in most ZK version, the <script> doesn't have <![CDATA[]]>, so, escape "&"
		response = response.replaceAll("[&]", "&amp;");
		Map<String, Object> map = AuUtility.parseAuResponseFromLayout(response);
		if (map != null) {
			process(desktopAgent, map);
		}
	}
}
