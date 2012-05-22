/* ColumnSortAgentBuilder.java

	Purpose:

	Description:

	History:
		2012/5/10 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.SortAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SortEvent;

/**
 * Create SortAgent object for Column.
 * 
 * @author Hawk
 *
 */
public class ColumnSortAgentBuilder implements OperationAgentBuilder<ComponentAgent,SortAgent> {

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

		/**
		 * For Column, it always send AU in spite of current sorted order.
		 */
		public void sort(boolean ascending) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_SORT;
			Component header= ((Component)target.getDelegatee());

			Map<String, Object> data = null;
			data = EventDataManager.getInstance().build(new SortEvent(cmd, header, ascending));
			((ClientCtrl) getClient()).postUpdate(desktopId, cmd, header.getUuid(), data, null);
			((ClientCtrl) getClient()).flush(desktopId);
		}
	}

}
