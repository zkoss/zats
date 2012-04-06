/* ConversationCtrl.java

	Purpose:
		
	Description:
		
	History:
		Apr 3, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.Map;

import org.zkoss.zats.mimic.DesktopAgent;

/**
 * Conversation controller interface.
 * To provide more control on conversation.
 * @author pao
 */
public interface ConversationCtrl {
	/**
	 * post an asynchronous update event.
	 * @param targetUUID the UUID of component agent which performed this event
	 * @param command command
	 * @param data data for update
	 */
	void postUpdate(String targetUUID, String cmd, Map<String, Object> data);
	
	/**
	 * clean current Desktop and release resources.
	 */
	void close();
	
	void destroy(DesktopAgent desktopAgent);
}
