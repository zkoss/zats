/* ClientCtrl.java

	Purpose:
		
	Description:
		
	History:
		Apr 3, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;

/**
 * Client controller interface.
 * To provide more control of the client for developers.
 * @author pao
 */
public interface ClientCtrl {
	/**
	 * post an asynchronous update event.
	 * It will be queued until invoke flush.
	 */
	void postUpdate(String desktopId, String cmd, String targetUUID, Map<String, Object> data,String option);
	
	/**
	 * flush the queued AU update event.
	 */
	void flush(String desktopId);
	
	void destroy(DesktopAgent desktopAgent);
	
	void setDestroyListener(DestroyListener l);
	
	//to notify a client is going to be destroyed.
	interface DestroyListener {
		void willDestroy(Client conv);
	}
	
	/**
	 * open a connect with current session and cookie.
	 * @param path the path to connect to.
	 * @throws IOException 
	 */
	InputStream openConnection(String path) throws IOException;
}
