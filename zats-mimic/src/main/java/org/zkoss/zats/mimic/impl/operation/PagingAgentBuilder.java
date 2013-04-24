/* PagingAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.PagingAgent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

/**
 * Agent builder for Paging.
 * @author Hawk
 *
 */
public class PagingAgentBuilder implements OperationAgentBuilder<ComponentAgent,PagingAgent>{
	public PagingAgent getOperation(final ComponentAgent target) {
		return new PagingAgentImpl(target);
	}

	public Class<PagingAgent> getOperationClass() {
		return PagingAgent.class;
	}
	
	class PagingAgentImpl extends AgentDelegator<ComponentAgent> implements PagingAgent{
		public PagingAgentImpl(ComponentAgent target) {
			super(target);
		}

		
		/* 
		 * Validate page index range first.
		 */
		public void moveTo(int pageIndex) {
			Paging paging= ((Paging)target.getDelegatee());

			if (pageIndex<0 || pageIndex>paging.getPageCount()-1){
				throw new AgentException("Page index out of bound (0-"+(paging.getPageCount()-1)+") : "+pageIndex);
			}
			
			String desktopId = target.getDesktop().getId();
			String cmd = ZulEvents.ON_PAGING;
			Map<String, Object> data = EventDataManager.getInstance().build(new PagingEvent(cmd, paging, pageIndex));
			((ClientCtrl)target.getClient()).postUpdate(desktopId, paging.getUuid(), cmd, data, false);
			((ClientCtrl) getClient()).flush(desktopId);
		}
	}


}
