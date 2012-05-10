/* GenericOpenAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;

/**
 * @author dennis
 *
 */
public class GenericOpenAgentBuilder implements OperationAgentBuilder<ComponentAgent,OpenAgent> {
	public OpenAgent getOperation(final ComponentAgent target) {
		return new OpenAgentImpl(target);
	}
	public Class<OpenAgent> getOperationClass() {
		return OpenAgent.class;
	}
	class OpenAgentImpl extends AgentDelegator<ComponentAgent> implements OpenAgent{
		public OpenAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void open(boolean open) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_OPEN;
			Map<String, Object> data = EventDataManager.getInstance().build(new OpenEvent(cmd, (Component)target.getDelegatee(), open, null,
					null));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}
	}

}
