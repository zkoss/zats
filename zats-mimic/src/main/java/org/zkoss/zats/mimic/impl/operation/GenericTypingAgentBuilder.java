/* GenericTypingAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 27, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.TypingAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * The generic implementation of typing agent.
 * @author pao
 */
public class GenericTypingAgentBuilder implements OperationAgentBuilder<TypingAgent> {
	public TypingAgent getOperation(ComponentAgent target) {
		return new TypingAgentImpl(target);
	}

	class TypingAgentImpl extends AgentDelegator implements TypingAgent {
		public TypingAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void typing(String value) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CHANGING;
			InputEvent event = new InputEvent(cmd, (Component) target.getDelegatee(), value, null);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}
	}
}
