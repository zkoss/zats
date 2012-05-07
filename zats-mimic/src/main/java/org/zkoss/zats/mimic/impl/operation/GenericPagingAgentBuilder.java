/* GenericPagingAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.PagingAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

/**
 * @author Hawk
 *
 */
public class GenericPagingAgentBuilder implements OperationAgentBuilder<ComponentAgent,PagingAgent>{
	public PagingAgent getOperation(final ComponentAgent target) {
		return new PagingAgentImpl(target);
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.impl.operation.OperationAgentBuilder#getOperationClass()
	 */
	public Class<PagingAgent> getOperationClass() {
		return PagingAgent.class;
	}
	
	class PagingAgentImpl extends AgentDelegator implements PagingAgent{
		public PagingAgentImpl(ComponentAgent target) {
			super(target);
		}

		
		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.PagingAgent#goTo(int)
		 */
		public void goTo(int page) {
			
			String desktopId = target.getDesktop().getId();
			String cmd = ZulEvents.ON_PAGING;
			Component paging= ((Component)target.getDelegatee());
			Map<String, Object> data = EventDataManager.build(new PagingEvent(cmd, paging, page));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, cmd, paging.getUuid(), data,null);
			
		}
	}


}
