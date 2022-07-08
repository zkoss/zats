/* RadioCheckAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Fri July 08 17:20:31 CST 2022, Created by jameschu

Copyright (C) 2022 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

public class RadioCheckAgentBuilder implements OperationAgentBuilder<ComponentAgent, CheckAgent> {
	public CheckAgent getOperation(final ComponentAgent target) {
		return new CheckAgentImpl(target);
	}

	public Class<CheckAgent> getOperationClass() {
		return CheckAgent.class;
	}

	class CheckAgentImpl extends AgentDelegator<ComponentAgent> implements CheckAgent {
		public CheckAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void check(boolean checked) {
			String desktopId = target.getDesktop().getId();
			Radio radio = target.as(Radio.class);
			Map<String, Object> data = EventDataManager.getInstance().build(new CheckEvent(Events.ON_CHECK, (Component) target.getDelegatee(), checked));
			ClientCtrl cctrl = (ClientCtrl) target.getClient();
			cctrl.postUpdate(desktopId, target.getUuid(), Events.ON_CHECK, data, false);
			cctrl.flush(desktopId);

			Radiogroup radiogroup = radio.getRadiogroup();
			if (radiogroup != null) {
				data = EventDataManager.getInstance().build(new CheckEvent(Events.ON_CHECK, radiogroup, checked));
				cctrl.postUpdate(desktopId, radiogroup.getUuid(), Events.ON_CHECK, data, false);
				cctrl.flush(desktopId);
			}
		}
	}
}
