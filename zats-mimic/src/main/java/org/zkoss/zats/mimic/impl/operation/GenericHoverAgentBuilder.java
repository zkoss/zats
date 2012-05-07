/* GenericHoverAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/5/2 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.HoverAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;

/**
 * @author Hawk
 *
 */
public class GenericHoverAgentBuilder implements OperationAgentBuilder<ComponentAgent,HoverAgent>{

	public HoverAgent getOperation(final ComponentAgent target) {
		return new HoverAgentImpl(target);
	}
	
	public Class<HoverAgent> getOperationClass() {
		return HoverAgent.class;
	}
	
	class HoverAgentImpl extends AgentDelegator<ComponentAgent> implements HoverAgent {

		/**
		 * @param target
		 */
		public HoverAgentImpl(ComponentAgent target) {
			super(target);
		}

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.HoverAgent#moveOver()
		 */
		public void moveOver() {
			// TODO Auto-generated method stub
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_MOUSE_OVER;
			Map<String, Object> data = EventDataManager.build(new MouseEvent(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.HoverAgent#moveOut()
		 */
		public void moveOut() {
			// TODO Auto-generated method stub
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_MOUSE_OUT;
			Map<String, Object> data = EventDataManager.build(new MouseEvent(cmd, (Component)target.getDelegatee()));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}
		
	}
}
