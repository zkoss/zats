/* GenericClickAgentBuilder.java

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
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
/**
 * 
 * @author pao
 *
 */
public class GenericClickAgentBuilder implements OperationAgentBuilder<ComponentAgent,ClickAgent> {
	public ClickAgent getOperation(final ComponentAgent target) {
		return new ClickAgentImpl(target);
	}
	public Class<ClickAgent> getOperationClass() {
		return ClickAgent.class;
	}

	class ClickAgentImpl extends AgentDelegator<ComponentAgent> implements ClickAgent {
		public ClickAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void click() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CLICK;
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}

		public void doubleClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_DOUBLE_CLICK;
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}

		public void rightClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_RIGHT_CLICK;
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}
	}
}
