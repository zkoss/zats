/* GenericCloseAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/4/6 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.CloseAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * @author Hawk
 *
 */
public class GenericCloseAgentBuilder implements OperationAgentBuilder<ComponentAgent,CloseAgent> {
	public CloseAgent getOperation(final ComponentAgent target) {
		return new CloseAgentImpl(target);
	}
	public Class<CloseAgent> getOperationClass() {
		return CloseAgent.class;
	}
	class CloseAgentImpl extends AgentDelegator<ComponentAgent> implements CloseAgent{
		public CloseAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void close() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CLOSE;
			Map<String, Object> data = EventDataManager.getInstance().build(new Event(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}
	}

}
