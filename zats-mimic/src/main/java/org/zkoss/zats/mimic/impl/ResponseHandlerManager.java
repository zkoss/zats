/* ResponseHandlerManager.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zats.mimic.impl.response.DownloadHandler;

/**
 * The manager of response handler.
 * It provide registry for handlers and let others access handlers.
 * @author pao
 */
public class ResponseHandlerManager {
	private static ResponseHandlerManager instance;

	public static synchronized ResponseHandlerManager getInstance() {
		if (instance == null) {
			instance = new ResponseHandlerManager();
		}
		return instance;
	}

	private List<LayoutResponseHandler> layoutHandlers;
	private List<UpdateResponseHandler> updateHandlers;

	public ResponseHandlerManager() {
		layoutHandlers = new ArrayList<LayoutResponseHandler>();
		updateHandlers = new ArrayList<UpdateResponseHandler>();

		// TODO layout response handler

		// AU response handler
		registerHandler("5.0.0", "*", new DownloadHandler());
	}

	public void registerHandler(String startVersion, String endVersion, String className) {
		try {
			// create object and check type
			Class<?> clazz = Class.forName(className);
			if (LayoutResponseHandler.class.isAssignableFrom(clazz))
				registerHandler(startVersion, endVersion, (LayoutResponseHandler) clazz.newInstance());
			else if (UpdateResponseHandler.class.isAssignableFrom(clazz))
				registerHandler(startVersion, endVersion, (UpdateResponseHandler) clazz.newInstance());
			else
				throw new ClassCastException(className + " neither layout response handler nor update response handler");
		} catch (Exception x) {
			throw new IllegalArgumentException(x.getMessage(), x);
		}
	}

	public void registerHandler(String startVersion, String endVersion, LayoutResponseHandler handler) {
		if (startVersion == null || endVersion == null || handler == null)
			throw new IllegalArgumentException();
		if (!Util.checkVersion(startVersion, endVersion))
			return;
		layoutHandlers.add(handler);
	}

	public void registerHandler(String startVersion, String endVersion, UpdateResponseHandler handler) {
		if (startVersion == null || endVersion == null || handler == null)
			throw new IllegalArgumentException();
		if (!Util.checkVersion(startVersion, endVersion))
			return;
		updateHandlers.add(handler);
	}

	public List<LayoutResponseHandler> getLayoutResponseHandlers() {
		return new ArrayList<LayoutResponseHandler>(layoutHandlers);
	}

	public List<UpdateResponseHandler> getUpdateResponseHandlers() {
		return new ArrayList<UpdateResponseHandler>(updateHandlers);
	}

}
