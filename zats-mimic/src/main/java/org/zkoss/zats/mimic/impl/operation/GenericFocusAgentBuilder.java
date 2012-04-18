/* GenericFocusAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

public class GenericFocusAgentBuilder implements OperationAgentBuilder<FocusAgent> {
	public FocusAgent getOperation(final ComponentAgent target) {
		return new FocusAgentImpl(target);
	}

	class FocusAgentImpl extends AgentDelegator implements FocusAgent {
		public FocusAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void focus() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_FOCUS;
			Map<String, Object> data = EventDataManager.build(new Event(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}

		public void blur() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_BLUR;
			Map<String, Object> data = EventDataManager.build(new Event(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}
	}
}
