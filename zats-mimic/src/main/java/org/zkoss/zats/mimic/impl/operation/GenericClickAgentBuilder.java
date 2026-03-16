/* GenericClickAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
/**
 * 
 * @author pao
 *
 */
public class GenericClickAgentBuilder implements OperationAgentBuilder<ComponentAgent,ClickAgent> {
	public ClickAgent getOperation(final ComponentAgent target) {
		return new ClickAgentImpl(target);
	}
	public Class<ClickAgent> getOperationClass() {
		return ClickAgent.class;
	}

	class ClickAgentImpl extends AgentDelegator<ComponentAgent> implements ClickAgent {
		public ClickAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void click() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CLICK;
			Component comp = (Component)target.getDelegatee();
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, comp, 0, 0, 0, 0, 0));
			ClientCtrl cctrl = (ClientCtrl)target.getClient();
			cctrl.postUpdate(desktopId, target.getUuid(), cmd, data, false);
			
			// For Menuitem with autocheck, ZK might expect an onCheck event alongside onClick
			if (comp instanceof org.zkoss.zul.Menuitem) {
				org.zkoss.zul.Menuitem mi = (org.zkoss.zul.Menuitem) comp;
				if (mi.isAutocheck()) {
					boolean checked = !mi.isChecked();
					Map<String, Object> checkData = EventDataManager.getInstance().build(new org.zkoss.zk.ui.event.CheckEvent(Events.ON_CHECK, mi, checked));
					cctrl.postUpdate(desktopId, target.getUuid(), Events.ON_CHECK, checkData, false);
				}
			}
			
			cctrl.flush(desktopId);
		}

		public void doubleClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_DOUBLE_CLICK;
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, (Component)target.getDelegatee(), 0, 0, 0, 0, 0));
			ClientCtrl cctrl = (ClientCtrl)target.getClient();
			cctrl.postUpdate(desktopId, target.getUuid(), cmd, data, false);
			cctrl.flush(desktopId);
		}

		public void rightClick() {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_RIGHT_CLICK;
			Map<String, Object> data = EventDataManager.getInstance().build(new MouseEvent(cmd, (Component)target.getDelegatee(), 0, 0, 0, 0, 0));
			ClientCtrl cctrl = (ClientCtrl) target.getClient();
			cctrl.postUpdate(desktopId, target.getUuid(), cmd, data, false);
			cctrl.flush(desktopId);
		}
	}
}
