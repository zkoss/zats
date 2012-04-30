/* GenericMaximizeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.lang.reflect.Method;
import java.util.Map;

import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.MaximizeAgent;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MaximizeEvent;
import org.zkoss.zk.ui.event.MinimizeEvent;

/**
 * The builder of maximize agent.
 * @author pao
 */
public class GenericMaximizeAgentBuilder implements OperationAgentBuilder<MaximizeAgent> {

	public MaximizeAgent getOperation(ComponentAgent target) {
		return new MaximizeAgentImpl(target);
	}

	private class MaximizeAgentImpl extends AgentDelegator implements MaximizeAgent {

		public MaximizeAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void setMaximized(boolean maximized) {
			check("Maximizable");
			HtmlBasedComponent comp = (HtmlBasedComponent) getDelegatee();
			String cmd = Events.ON_MAXIMIZE;
			MaximizeEvent event = new MaximizeEvent(cmd, comp, "", "", comp.getWidth(), comp.getHeight(), maximized);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), target.getUuid(), cmd, data);
		}

		public void setMinimized(boolean minimized) {
			check("Minimizable");
			HtmlBasedComponent comp = (HtmlBasedComponent) getDelegatee();
			String cmd = Events.ON_MINIMIZE;
			MinimizeEvent event = new MinimizeEvent(cmd, comp, "", "", comp.getWidth(), comp.getHeight(), minimized);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), target.getUuid(), cmd, data);
		}

		/**
		 * check enabled flag.
		 */
		private void check(String property) {
			try {
				Method method = getDelegatee().getClass().getMethod("is" + property);
				boolean maximizable = (Boolean) method.invoke(getDelegatee());
				if (!maximizable)
					throw new AgentException("component isn't " + property.toLowerCase());
			} catch (AgentException e) {
				throw e;
			} catch (Exception e) {
				throw new ZatsException(getDelegatee().getClass().getName() + " doesn't support "
						+ property.toLowerCase() + " operation", e);
			}
		}
	}
}
