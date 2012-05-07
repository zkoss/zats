/* TextboxOpenAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/4/9 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Textbox;

/**
 * A implementation of open agent builder for sub-class of textbox. 
 * @author pao
 */
public class TextboxOpenAgentBuilder implements OperationAgentBuilder<ComponentAgent, OpenAgent> {
	public OpenAgent getOperation(final ComponentAgent target) {
		return new OpenAgentImpl(target);
	}
	public Class<OpenAgent> getOperationClass() {
		return OpenAgent.class;
	}
	class OpenAgentImpl extends AgentDelegator implements OpenAgent {
		public OpenAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void open(boolean open) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_OPEN;

			String value = target.as(Textbox.class).getValue();
			OpenEvent event = new OpenEvent(cmd, (Component)target.getDelegatee(), open, null, value);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId,cmd, target.getUuid(), data, null);
		}
	}

}
