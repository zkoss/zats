/* AuEventAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 22, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.AuEvent;
import org.zkoss.zats.mimic.operation.AuEventAgent;

/**
 * The builder for AU event agent.
 * @author pao
 * @since 1.1.0
 */
public class AuEventAgentBuilder implements OperationAgentBuilder<ComponentAgent, AuEventAgent> {

	public AuEventAgent getOperation(ComponentAgent agent) {
		return new AuEventAgentImpl(agent);
	}

	public Class<AuEventAgent> getOperationClass() {
		return AuEventAgent.class;
	}

	class AuEventAgentImpl extends AgentDelegator<ComponentAgent> implements AuEventAgent {

		public AuEventAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void post(AuEvent... events) {
			ClientCtrl client = (ClientCtrl) target.getClient();
			String desktopId = target.getDesktop().getId();
			for (AuEvent event : events) {
				if(event != null) {
					client.postUpdate(desktopId, event.getName(), target.getUuid(), event.getData(), null);
				} else {
					throw new AgentException("an AU event for posting is null");
				}
			}
			client.flush(desktopId);
		}
	}

}
