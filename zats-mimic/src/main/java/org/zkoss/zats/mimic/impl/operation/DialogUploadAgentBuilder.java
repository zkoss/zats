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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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

/**
 * The builder implementation of dialog upload agent
 * @author pao
 * @since 1.1.0
 */
public class DialogUploadAgentBuilder implements OperationAgentBuilder<DesktopAgent, UploadAgent> {
	private final static Logger logger = Logger.getLogger(DialogUploadAgentBuilder.class.getName());

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

			Map data = new HashMap<>();
			data.put("file", new AbstractUploadAgentBuilder.FileItem(fileName, content, contentType));
			((ClientCtrl) target.getClient()).postUpdate(target.getId(), fileupload.getUuid(), Events.ON_UPLOAD, data,
					false);
			((ClientCtrl) target.getClient()).flush(target.getId());
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
			((ClientCtrl) getClient()).postUpdate(desktopId, dialog.getUuid(), Events.ON_CLOSE, data, false);
			((ClientCtrl) getClient()).flush(desktopId);
		}
	}
}