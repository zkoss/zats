/* AuLogHandler.java

	Purpose:
		
	Description:
		
	History:
		Mon Apr 29 18:01:24 CST 2019, Created by rudyhuang

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.zkoss.lang.Objects;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.DefaultDesktopAgent;
import org.zkoss.zats.mimic.impl.DesktopCtrl;
import org.zkoss.zats.mimic.impl.LayoutResponseHandler;
import org.zkoss.zats.mimic.impl.UpdateResponseHandler;
import org.zkoss.zats.mimic.impl.au.AuUtility;
import org.zkoss.zk.au.AuResponse;

/**
 * The response handler for AuLog event.
 *
 * @author rudyhuang
 * @since 2.0.1
 */
public class AuLogHandler implements UpdateResponseHandler, LayoutResponseHandler {
	public final static String REGISTER_KEY = "log";
	private final static Logger logger = Logger.getLogger(AuLogHandler.class.getName());

	@Override
	public void process(DesktopAgent desktopAgent, String response) {
		// ZATS-11: in most ZK version, the <script> doesn't have <![CDATA[]]>, so, escape "&"
		response = response.replaceAll("[&]", "&amp;");
		Map<String, Object> map = AuUtility.parseAuResponseFromLayout(response);
		if (map != null) {
			process(desktopAgent, map);
		}
	}

	@Override
	public void process(DesktopAgent desktop, Map<String, Object> jsonObject) {
		// fetch all commands and find log event
		List<AuResponse> responses = AuUtility.convertToResponses(jsonObject);
		for (AuResponse resp : responses) {
			if (REGISTER_KEY.equals(resp.getCommand())) {
				Object[] rawData = resp.getRawData();
				((DesktopCtrl) desktop).appendZkLog(Objects.toString(rawData[0]));
			}
		}
	}
}
