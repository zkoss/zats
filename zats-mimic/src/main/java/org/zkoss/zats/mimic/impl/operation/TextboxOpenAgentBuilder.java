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
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Textbox;

/**
 * @author pao
 */
public class TextboxOpenAgentBuilder implements OperationAgentBuilder<OpenAgent> {
	public OpenAgent getOperation(final ComponentAgent target) {
		return new OpenAgentImpl(target);
	}

	class OpenAgentImpl extends AgentDelegator implements OpenAgent {
		public OpenAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void open(boolean open) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_OPEN;

			String value = ((Textbox) target).getValue();
			OpenEvent event = new OpenEvent(cmd, target.getComponent(), open, null, value);
			Map<String, Object> data = EventDataManager.build(event);
			((ConversationCtrl) target.getConversation()).postUpdate(desktopId, target.getUuid(), cmd, data);
		}
	}

}
