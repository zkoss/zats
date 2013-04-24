/* AbstractMultipleSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 9, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation.select;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.AgentDelegator;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;

/**
 * An abstract builder, because different components have different ways to get its status.
 * @author pao
 *
 */
public abstract class AbstractMultipleSelectAgentBuilder implements OperationAgentBuilder<ComponentAgent, MultipleSelectAgent>{
	
	
	public Class<MultipleSelectAgent> getOperationClass() {
		return MultipleSelectAgent.class;
	}
	
	static public abstract class AbstractMultipleSelectAgentImpl extends AgentDelegator<ComponentAgent> implements MultipleSelectAgent {
		public AbstractMultipleSelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void select() {
			// check if parent component is at multiple selection mode
			if (isMultiple()) {
				Set<String> selected = new HashSet<String>();
				collectSelectedItems(selected);
				if (!selected.contains(target.getUuid())) {
					selected.add(target.getUuid());
					postUpdate(getEventTarget(), selected);
				} else
					return; // skip, target was already selected.
			} else {
				throw new AgentException(target + " isn't multiple selection mode");
			}
		}

		public void deselect() {
			// check if parent component is at multiple selection mode
			if (isMultiple()) {
				Set<String> selected = new HashSet<String>();
				collectSelectedItems(selected);
				if (selected.contains(target.getUuid())) {
					selected.remove(target.getUuid());
					postUpdate(getEventTarget(), selected);
				} else
					return; // skip, target wasn't selected.
			} else {
				throw new AgentException(target + " isn't multiple selection mode");
			}
		}

		private void postUpdate(Component component, Set<String> selected) {
			String desktopId = target.getDesktop().getId();
			Event event = new SelectEvent(Events.ON_SELECT, component, selected, (Component)target.getDelegatee());
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			ClientCtrl ctrl = (ClientCtrl) target.getClient();
			ctrl.postUpdate(desktopId, component.getUuid(), event.getName(), data, false);
			ctrl.flush(desktopId);
		}

		abstract protected Component getEventTarget();

		abstract protected boolean isMultiple();

		abstract protected void collectSelectedItems(Set<String> selected);
	}
}
