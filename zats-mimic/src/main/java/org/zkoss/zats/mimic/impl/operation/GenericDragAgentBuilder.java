/* GenericDragAgentBuilder.java

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
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.DragAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Events;

/**
 * Mimic drag & drop. HtmlBasedComponent supported.
 * @author Hawk
 *
 */
public class GenericDragAgentBuilder implements OperationAgentBuilder<ComponentAgent,DragAgent> {

	public DragAgent getOperation(final ComponentAgent target) {
		return new DragAgentImpl(target);
	}
	
	public Class<DragAgent> getOperationClass() {
		return DragAgent.class;
	}
	
	class DragAgentImpl extends AgentDelegator<ComponentAgent> implements DragAgent {
		
		public DragAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void dropOn(ComponentAgent dropTarget) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_DROP;
			Map<String, Object> data = EventDataManager.getInstance().build(new DropEvent(cmd,(Component)dropTarget.getDelegatee(), (Component)target.getDelegatee(),0,0,0,0,0));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, dropTarget.getUuid(), cmd, data, false);
			((ClientCtrl)target.getClient()).flush(desktopId);
		}
	}

}
