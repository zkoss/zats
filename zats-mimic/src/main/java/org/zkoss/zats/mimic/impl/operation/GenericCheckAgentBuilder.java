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
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;

public class GenericCheckAgentBuilder implements OperationAgentBuilder<CheckAgent> {

	public CheckAgent getOperation(final ComponentAgent target) {
		return new CheckAgentImpl(target);
	}
	
	class CheckAgentImpl extends AgentDelegator implements CheckAgent{
		public CheckAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void check(boolean checked) {
			String desktopId = target.getDesktop().getId();
			Map<String, Object> data = EventDataManager.build(new CheckEvent(Events.ON_CHECK, target.getComponent(),
					checked));
			ConversationCtrl cctrl = (ConversationCtrl) target.getConversation();
			cctrl.postUpdate(desktopId, target.getUuid(), Events.ON_CHECK, data);
		}
	}
}
