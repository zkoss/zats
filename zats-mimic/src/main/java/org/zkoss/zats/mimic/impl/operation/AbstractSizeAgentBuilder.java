/* GenericSizeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.SizeAgent;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MaximizeEvent;
import org.zkoss.zk.ui.event.MinimizeEvent;
import org.zkoss.zk.ui.event.SizeEvent;

/**
 * The builder for size agent.
 * @author pao
 * @author dennis
 */
public abstract class AbstractSizeAgentBuilder implements OperationAgentBuilder<ComponentAgent,SizeAgent> {

	public Class<SizeAgent> getOperationClass() {
		return SizeAgent.class;
	}
	abstract static class AbstractSizeAgentImpl extends AgentDelegator<ComponentAgent> implements SizeAgent {
		public AbstractSizeAgentImpl(ComponentAgent target) {
			super(target);
		}
		
		abstract protected boolean isMaximizable();
		abstract protected boolean isMinimizable();
		abstract protected boolean isSizable();
		abstract protected int getMinwidth();
		abstract protected int getMinheight();
		

		public void maximize(boolean maximized) {
			if(!isMaximizable()){
				throw new AgentException(target + " is not maximizable");
			}
			HtmlBasedComponent comp = target.as(HtmlBasedComponent.class);
			String cmd = Events.ON_MAXIMIZE;
			MaximizeEvent event = new MaximizeEvent(cmd, comp, "", "", comp.getWidth(), comp.getHeight(), maximized);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
		}

		public void minimize(boolean minimized) {
			if(!isMinimizable()){
				throw new AgentException(target + " is not minimizable");
			}
			HtmlBasedComponent comp = target.as(HtmlBasedComponent.class);
			String cmd = Events.ON_MINIMIZE;
			MinimizeEvent event = new MinimizeEvent(cmd, comp, "", "", comp.getWidth(), comp.getHeight(), minimized);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
		}

		public void resize(int width, int height) {
			if(!isSizable()){
				throw new AgentException(target + " is not sizable");
			}
			
			if (width < 0 && height < 0)
				return; // do nothing

			// check flag
			HtmlBasedComponent comp = target.as(HtmlBasedComponent.class);
			// check arguments
			try {
				if (width < 0) {
					String widthStr = comp.getWidth();
					if (widthStr == null || widthStr.length() <= 0) {
						widthStr = getMinwidth() + "px";
					}
					width = Integer.parseInt(widthStr.substring(0, widthStr.length() - 2));
				}
				if (height < 0) {
					String heightStr = comp.getHeight();
					if (heightStr == null || heightStr.length() <= 0) {
						heightStr = getMinheight() + "px";
					}
					height = Integer.parseInt(heightStr.substring(0, heightStr.length() - 2));
				}
			} catch (Exception e) {
				throw new AgentException(e.getMessage(), e);
			}

			// post AU
			String cmd = Events.ON_SIZE;
			SizeEvent event = new SizeEvent(cmd, comp, width + "px", height + "px", 0);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
		}
	}
}
