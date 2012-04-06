/* GenericClickAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;

public class GenericClickAgentBuilder implements OperationAgentBuilder<ClickAgent> {
	public ClickAgent getOperation(final ComponentAgent target) {
		return new ClickAgentImpl(target);
	}

	class ClickAgentImpl extends AgentDelegator implements ClickAgent {
		public ClickAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void click() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CLICK;
			Map<String, Object> data = EventDataManager.build(new MouseEvent(cmd, target.getComponent()));
			((ConversationCtrl)target.getConversation()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}

		public void doubleClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_DOUBLE_CLICK;
			Map<String, Object> data = EventDataManager.build(new MouseEvent(cmd, target.getComponent()));
			((ConversationCtrl)target.getConversation()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}

		public void rightClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_RIGHT_CLICK;
			Map<String, Object> data = EventDataManager.build(new MouseEvent(cmd, target.getComponent()));
			((ConversationCtrl)target.getConversation()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}
	}
}
