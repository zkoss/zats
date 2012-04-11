/* ClientCtrl.java

	Purpose:
		
	Description:
		
	History:
		Apr 3, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;

/**
 * Client controller interface.
 * To provide more control of the client.
 * @author pao
 */
public interface ClientCtrl {
	/**
	 * post an asynchronous update event.
	 * @param desktopId TODO
	 * @param targetUUID the UUID of component agent which performed this event
	 * @param data data for update
	 * @param command command
	 */
	void postUpdate(String desktopId, String targetUUID, String cmd, Map<String, Object> data);
	
	void destroy(DesktopAgent desktopAgent);
	
	void setDestroyListener(DestroyListener l);
	
	HttpSession getSession();
	
	//to notify a client is going to be destroyed.
	interface DestroyListener {
		void willDestroy(Client conv);
	}
	
}
