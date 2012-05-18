/* GenericMoveAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 8, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.MoveAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MoveEvent;

/**
 * The generic builder of move agent. 
 * @author pao
 */
public class GenericMoveAgentBuilder implements OperationAgentBuilder<ComponentAgent, MoveAgent> {

	public Class<MoveAgent> getOperationClass() {
		return MoveAgent.class;
	}

	public MoveAgent getOperation(ComponentAgent agent) {
		return new MoveAgentImpl(agent);
	}

	private class MoveAgentImpl extends AgentDelegator<ComponentAgent> implements MoveAgent {
		public MoveAgentImpl(ComponentAgent agent) {
			super(agent);
		}

		public void moveTo(int left, int top) {
			String cmd = Events.ON_MOVE;
			MoveEvent event = new MoveEvent(cmd, target.as(Component.class), left + "px", top + "px", 0);
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			String desktopId = target.getDesktop().getId();
			((ClientCtrl) getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
			((ClientCtrl) getClient()).flush(desktopId);			
		}
	}
}
