/* DialogUploadAgent.java

	Purpose:
		
	Description:
		
	History:
		Jun 19, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.util.IO;
import org.zkoss.lang.Strings;
import org.zkoss.zats.common.util.MultiPartOutputStream;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.DesktopCtrl;

/**
 * The dialog upload agent implementation.
 * @author pao
 * @since 1.1.0
 */
public class DialogUploadAgent implements OperationAgentBuilder<DesktopAgent, UploadAgent> {
	private final static Logger logger = Logger.getLogger(DialogUploadAgent.class.getName());

	public Class<UploadAgent> getOperationClass() {
		return UploadAgent.class;
	}

	public UploadAgent getOperation(DesktopAgent target) {
		return new UploadAgentImpl(target);
	}

	class UploadAgentImpl extends AgentDelegator<DesktopAgent> implements UploadAgent {

		private int sid; // serial ID. for next file

		public UploadAgentImpl(DesktopAgent target) {
			super(target);
		}

		public void upload(File file, String contentType) {
			if (file == null)
				throw new NullPointerException("file can't be null.");

			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(file));
				upload(file.getName(), is, contentType);
			} catch (IOException e) {
				throw new AgentException(e.getMessage(), e);
			} finally {
				Util.close(is);
			}
		}

		public void upload(String fileName, InputStream content, String contentType) {
			if (fileName == null)
				throw new NullPointerException("file name can't be null.");
			if (content == null)
				throw new NullPointerException("content stream can't be null.");

			// find upload dialog
			DesktopAgent desktop = target;
			ComponentAgent dialog = desktop.query("uploaddlg");
			ComponentAgent fileupload = desktop.query("uploaddlg fileupload");
			if (dialog == null || fileupload == null)
				throw new AgentException("There is no dialog for uploading.");

			OutputStream os = null;
			InputStream is = null;
			try {
				// parameters 
				String param = "?uuid={0}&dtid={1}&sid={2}&maxsize=undefined";
				param = MessageFormat.format(param, fileupload.getUuid(), desktop.getId(), String.valueOf(sid));
				// open connection
				String boundary = MultiPartOutputStream.generateBoundary(); // boundary for multipart
				ClientCtrl cc = (ClientCtrl) getClient();
				HttpURLConnection conn = cc.getConnection("/zkau/upload" + param, "POST");
				conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				os = conn.getOutputStream();
				MultiPartOutputStream multipartStream = new MultiPartOutputStream(os, boundary);
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("desktop: " + desktop.getId());
					logger.finest("upload URL: " + conn.getURL().toString());
				}

				// additional headers
				String contentDisposition = "Content-Disposition: form-data; name=\"file\"; filename=\"{0}\"";
				contentDisposition = MessageFormat.format(contentDisposition, fileName);
				String[] headers = new String[] { contentDisposition };
				// upload multipart data
				multipartStream.startPart(contentType != null ? contentType : "application/octet-stream", headers); // default content type
				int b;
				while ((b = content.read()) >= 0)
					multipartStream.write(b);
				Util.close(multipartStream); // close before get input stream
				// read response
				String respMsg = conn.getResponseMessage();
				is = conn.getInputStream();
				String resp = IO.toString(is);
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("response message: " + respMsg);
					logger.finest("response content: " + resp);
				}

			} catch (IOException e) {
				throw new AgentException(e.getMessage(), e);
			} finally {
				Util.close(os);
				Util.close(is);
			}

			// get the correct key
			int key = ((DesktopCtrl) desktop.getDelegatee()).getNextKey() - 1;
			String contentId = Strings.encode(new StringBuffer(12).append("z__ul_"), key).toString(); // copy from AuUploader
			// perform AU
			String cmd = "updateResult";
			String desktopId = desktop.getId();
			Event event = new Event(cmd, (Component) fileupload.getDelegatee());
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			data.put("wid", fileupload.getUuid());
			data.put("contentId", contentId);
			data.put("sid", String.valueOf(sid++)); // increase sid
			((ClientCtrl) getClient()).postUpdate(desktopId, cmd, fileupload.getUuid(), data, null);
			((ClientCtrl) getClient()).flush(desktopId);
		}

		public void finish() {

			// find upload dialog
			DesktopAgent desktop = target;
			ComponentAgent dialog = desktop.query("uploaddlg");
			if (dialog == null)
				throw new AgentException("There is no dialog for uploading.");

			// perform AU to close dialog
			String desktopId = desktop.getId();
			Event event = new Event(Events.ON_CLOSE, (Component) dialog.getDelegatee());
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			data.put("", true);
			((ClientCtrl) getClient()).postUpdate(desktopId, Events.ON_CLOSE, dialog.getUuid(), data, null);
			((ClientCtrl) getClient()).flush(desktopId);

			// reset
			sid = 0;
		}
	}
}
