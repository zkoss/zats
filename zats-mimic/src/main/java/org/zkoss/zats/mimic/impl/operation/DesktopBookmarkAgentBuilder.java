/* DesktopBookmarkAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.BookmarkAgent;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Events;

/**
 * @author dennis
 *
 */
public class DesktopBookmarkAgentBuilder implements OperationAgentBuilder<DesktopAgent,BookmarkAgent>{
	
	public BookmarkAgent getOperation(DesktopAgent agent) {
		return new BookmarkAgentImpl(agent);
	}

	public Class<BookmarkAgent> getOperationClass() {
		return BookmarkAgent.class;
	}

	class BookmarkAgentImpl extends AgentDelegator<DesktopAgent> implements BookmarkAgent{
		public BookmarkAgentImpl(DesktopAgent target) {
			super(target);
		}

		public void change(String value) {
			String desktopId = target.getId();
			ClientCtrl cctrl = (ClientCtrl) target.getClient();
			Map<String, Object> data = EventDataManager.getInstance().build(new BookmarkEvent(Events.ON_BOOKMARK_CHANGE, value));
			cctrl.postUpdate(desktopId, null, Events.ON_BOOKMARK_CHANGE, data, false);
			cctrl.flush(desktopId);
		}
	}
}
