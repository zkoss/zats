/* ButtonUploadAgent.java

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.util.IO;

import org.zkoss.zats.common.util.MultiPartOutputStream;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zk.ui.event.Events;

/**
 * The abstract builder of upload agent.
 * @author pao
 * @since 1.1.0
 */
public abstract class AbstractUploadAgentBuilder implements OperationAgentBuilder<ComponentAgent, UploadAgent> {
	private final static Logger logger = Logger.getLogger(AbstractUploadAgentBuilder.class.getName());

	public Class<UploadAgent> getOperationClass() {
		return UploadAgent.class;
	}

	public static class FileItem {
		String contentType;
		String fileName;
		InputStream inputStream;

		public FileItem(File file, String contentType) throws FileNotFoundException {
			this.inputStream = new FileInputStream(file);
			this.fileName = file.getName();
			this.contentType = contentType;
		}

		public FileItem(String fileName, InputStream file, String contentType) {
			this.fileName = fileName;
			this.inputStream = file;
			this.contentType = contentType;
		}

		public String getFileName() {
			return fileName;
		}

		public String getContentType() {
			return contentType;
		}

		public InputStream getInputStream() {
			return inputStream;
		}
	}

	abstract class AbstractUploadAgentImpl extends AgentDelegator<ComponentAgent> implements UploadAgent {

		private HttpURLConnection conn;
		private MultiPartOutputStream multipartStream;
		private boolean isMultiple;

		public AbstractUploadAgentImpl(ComponentAgent target) {
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

		protected abstract String getUploadFlag();

		public void upload(String fileName, InputStream content, String contentType) {
			if (fileName == null)
				throw new NullPointerException("file name can't be null.");
			if (content == null)
				throw new NullPointerException("content stream can't be null.");

			Map data = new HashMap<>();
			data.put("file", new FileItem(fileName, content, contentType));
			((ClientCtrl) target.getClient()).postUpdate(target.getDesktop().getId(), target.getUuid(),
					Events.ON_UPLOAD, data, false);
		}

		public void finish() {
			((ClientCtrl) target.getClient()).flush(target.getDesktop().getId());
		}

		private void clean() {
			// close output
			Util.close(multipartStream);
			multipartStream = null;

			// close input 
			InputStream is = null;
			try {
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
				Util.close(is);
				conn = null;
			}
		}
	}
}