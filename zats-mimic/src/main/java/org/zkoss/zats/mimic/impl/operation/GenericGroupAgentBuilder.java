/* GenericGroupAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 7, 2012 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.GroupAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * The builder for group agent.
 * @author Hawk
 */
public class GenericGroupAgentBuilder implements OperationAgentBuilder<ComponentAgent,GroupAgent> {

	public GroupAgent getOperation(ComponentAgent target) {
		return new GroupAgentImpl(target);
	}
	
	public Class<GroupAgent> getOperationClass() {
		return GroupAgent.class;
	}
	
	private class GroupAgentImpl extends AgentDelegator<ComponentAgent> implements GroupAgent {
		public GroupAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void group() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_GROUP;
			Map<String, Object> data = EventDataManager.build(new Event(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
			
		}

		
	}
}
