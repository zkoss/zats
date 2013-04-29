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
import org.zkoss.zats.mimic.operation.AuData;
import org.zkoss.zats.mimic.operation.AuAgent;

/**
 * The builder for AU request agent.
 * @author pao
 * @since 1.1.0
 */
public class AuAgentBuilder implements OperationAgentBuilder<ComponentAgent, AuAgent> {

	public AuAgent getOperation(ComponentAgent agent) {
		return new AuAgentImpl(agent);
	}

	public Class<AuAgent> getOperationClass() {
		return AuAgent.class;
	}

	class AuAgentImpl extends AgentDelegator<ComponentAgent> implements AuAgent {

		public AuAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void post(AuData... events) {
			ClientCtrl client = (ClientCtrl) target.getClient();
			String desktopId = target.getDesktop().getId();
			for (AuData event : events) {
				if(event != null) {
					client.postUpdate(desktopId, target.getUuid(), event.getName(), event.getData(), false);
				} else {
					throw new AgentException("an AU event for posting is null");
				}
			}
			client.flush(desktopId);
		}
	}

}
