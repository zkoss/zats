/* ConversationCtrl.java

	Purpose:
		
	Description:
		
	History:
		Apr 3, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.Map;

import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.DesktopAgent;

/**
 * Conversation controller interface.
 * To provide more control on conversation.
 * @author pao
 */
public interface ConversationCtrl {
	/**
	 * post an asynchronous update event.
	 * @param desktopId TODO
	 * @param targetUUID the UUID of component agent which performed this event
	 * @param data data for update
	 * @param command command
	 */
	void postUpdate(String desktopId, String targetUUID, String cmd, Map<String, Object> data);
	
	void destroy(DesktopAgent desktopAgent);
	
	void setCloseListener(CloseListener l);
	
	//to notify a conversation is going to be closed.
	interface CloseListener {
		void willClose(Conversation conv);
	}
	
}
