/* DownloadHandler.java

	Purpose:
		
	Description:
		
	History:
		May 28, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Resource;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.DesktopCtrl;
import org.zkoss.zats.mimic.impl.UpdateResponseHandler;
import org.zkoss.zats.mimic.impl.au.AuUtility;
import org.zkoss.zk.au.AuResponse;

/**
 * The response handler for download event.
 * @author pao
 */
public class DownloadHandler implements UpdateResponseHandler {

	private static Logger logger = Logger.getLogger(DownloadHandler.class.getName());

	public void process(DesktopAgent desktop, Map<String, Object> jsonObject) {

		// clean downloadable file first
		((DesktopCtrl) desktop).setDownloadable(null);

		// fetch all commands and find the last download command
		List<AuResponse> resp = AuUtility.convertToResponses(jsonObject);
		Map<String, AuResponse> map = new HashMap<String, AuResponse>();
		for (AuResponse r : resp)
			map.put(r.getCommand(), r);
		AuResponse download = map.get("download");
		
		// get path and create downloadable file
		if (download != null) {
			Object[] data = download.getRawData();
			String path = data[0].toString().replaceAll("\\\\", ""); // remove unnecessary char.
			if (logger.isLoggable(Level.FINEST))
				logger.finest("download event: " + path);
			Resource downloadable = new DownloadableImpl(desktop, path);
			((DesktopCtrl) desktop).setDownloadable(downloadable);
		}
	}

	private static class DownloadableImpl implements Resource {

		private DesktopAgent desktop;
		private String path;

		public DownloadableImpl(DesktopAgent desktop, String path) {
			this.desktop = desktop;
			this.path = path;
		}

		public String getName() {
			return path.substring(path.lastIndexOf("/") + 1);
		}

		public InputStream getInputStream() throws IOException{
			Client client = desktop.getClient();
			ClientCtrl cc = (ClientCtrl) client;
			return cc.openConnection(path);
		}
	}
}
