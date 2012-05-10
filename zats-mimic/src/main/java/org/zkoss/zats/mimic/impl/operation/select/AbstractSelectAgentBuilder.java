/* GenericSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

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
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;

/**
 *  The builder for selection agent.
 * @author pao
 */
public abstract class AbstractSelectAgentBuilder implements OperationAgentBuilder<ComponentAgent, SelectAgent>{
	
	
	public Class<SelectAgent> getOperationClass() {
		return SelectAgent.class;
	}

	static public abstract class AbstractSelectAgentImpl extends AgentDelegator<ComponentAgent> implements SelectAgent {
		public AbstractSelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		abstract protected Component getEventTarget();

		//subclass contribute extra info to do postUpdate
		abstract protected void contributeExtraInfo(Map<String, Object> data);

		public void select() {
			Set<String> items = new HashSet<String>();
			items.add(target.getUuid());

			Component ancestry = getEventTarget();
			Event event = new SelectEvent(Events.ON_SELECT, ancestry, items, (Component)target.getDelegatee());
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			contributeExtraInfo(data);
			((ClientCtrl) target.getClient()).postUpdate(target.getDesktop().getId(), event.getName(), ancestry.getUuid(),
					data, null);
		}

		public void deselect() {
			throw new AgentException(target + " can't deselect");
		}
	}
}
