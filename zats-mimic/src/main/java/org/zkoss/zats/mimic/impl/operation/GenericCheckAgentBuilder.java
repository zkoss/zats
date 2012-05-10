/* GenericCheckAgentBuilder.java

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
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;

public class GenericCheckAgentBuilder implements OperationAgentBuilder<ComponentAgent,CheckAgent> {

	public CheckAgent getOperation(final ComponentAgent target) {
		return new CheckAgentImpl(target);
	}
	public Class<CheckAgent> getOperationClass() {
		return CheckAgent.class;
	}
	class CheckAgentImpl extends AgentDelegator<ComponentAgent> implements CheckAgent{
		public CheckAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void check(boolean checked) {
			String desktopId = target.getDesktop().getId();
			Map<String, Object> data = EventDataManager.build(new CheckEvent(Events.ON_CHECK, (Component)target.getDelegatee(),
					checked));
			ClientCtrl cctrl = (ClientCtrl) target.getClient();
			cctrl.postUpdate(desktopId, Events.ON_CHECK, target.getUuid(), data, null);
		}
	}
}
