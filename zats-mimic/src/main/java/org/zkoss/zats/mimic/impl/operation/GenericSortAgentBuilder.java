/* GenericSortAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.SortAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SortEvent;

/**
 * @author Hawk
 *
 */
public class GenericSortAgentBuilder implements OperationAgentBuilder<ComponentAgent,SortAgent> {
	
	public SortAgent getOperation(ComponentAgent target) {
		return new SortAgentImpl(target);
	}
	public Class<SortAgent> getOperationClass() {
		return SortAgent.class;
	}
	private class SortAgentImpl extends AgentDelegator<ComponentAgent> implements SortAgent {
		public SortAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void sort(boolean ascending) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_SORT;
			Component paging= ((Component)target.getDelegatee());
			Map<String, Object> data = EventDataManager.build(new SortEvent(cmd, paging, ascending));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, paging.getUuid(), data, null);
		}
	}

}
