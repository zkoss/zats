/* ResponseHandlerManager.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.mimic.impl.response.DownloadHandler;
import org.zkoss.zats.mimic.impl.response.EchoEventHandler;
import org.zkoss.zats.mimic.impl.response.AuLogHandler;

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

	private Map<String, LayoutResponseHandler> layoutHandlers;
	private Map<String, UpdateResponseHandler> updateHandlers;

	public ResponseHandlerManager() {
		layoutHandlers = new HashMap<String, LayoutResponseHandler>();
		updateHandlers = new HashMap<String, UpdateResponseHandler>();

		// layout response handler
		registerHandler("5.0.0", "*", DownloadHandler.REGISTER_KEY, (LayoutResponseHandler)new DownloadHandler());
		registerHandler("5.0.0", "5.*.*", EchoEventHandler.REGISTER_KEY, (LayoutResponseHandler)new EchoEventHandler());
		registerHandler("5.0.0", "*", AuLogHandler.REGISTER_KEY, (LayoutResponseHandler)new AuLogHandler());

		// AU response handler
		registerHandler("5.0.0", "*", DownloadHandler.REGISTER_KEY, (UpdateResponseHandler)new DownloadHandler());
		registerHandler("5.0.0", "5.*.*", EchoEventHandler.REGISTER_KEY, (UpdateResponseHandler)new EchoEventHandler());
		registerHandler("5.0.0", "*", AuLogHandler.REGISTER_KEY, (UpdateResponseHandler)new AuLogHandler());
	}

	public void registerHandler(String startVersion, String endVersion, String key, String className) {
		try {
			// create object and check type
			Class<?> clazz = Class.forName(className);
			if (LayoutResponseHandler.class.isAssignableFrom(clazz))
				registerHandler(startVersion, endVersion, key, (LayoutResponseHandler) clazz.newInstance());
			else if (UpdateResponseHandler.class.isAssignableFrom(clazz))
				registerHandler(startVersion, endVersion, key, (UpdateResponseHandler) clazz.newInstance());
			else
				throw new ClassCastException(className + " neither layout response handler nor update response handler");
		} catch (Exception x) {
			throw new IllegalArgumentException(x.getMessage(), x);
		}
	}

	public void registerHandler(String startVersion, String endVersion, String key, LayoutResponseHandler handler) {
		if (startVersion == null || endVersion == null || key == null || handler == null)
			throw new IllegalArgumentException();
		if (!Util.checkVersion(startVersion, endVersion))
			return;
		// ZATS-11: note that, the key can be used for replacing previous one and prevent duplicate handlers
		layoutHandlers.put(key, handler);
	}

	public void registerHandler(String startVersion, String endVersion, String key, UpdateResponseHandler handler) {
		if (startVersion == null || endVersion == null || key == null || handler == null)
			throw new IllegalArgumentException();
		if (!Util.checkVersion(startVersion, endVersion))
			return;
		// ZATS-11: note that, the key can be used for replacing previous one and prevent duplicate handlers
		updateHandlers.put(key, handler);
	}

	public List<LayoutResponseHandler> getLayoutResponseHandlers() {
		return new ArrayList<LayoutResponseHandler>(layoutHandlers.values());
	}

	public List<UpdateResponseHandler> getUpdateResponseHandlers() {
		return new ArrayList<UpdateResponseHandler>(updateHandlers.values());
	}

}
