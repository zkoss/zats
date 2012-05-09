/* SliderInputAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 8, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigDecimal;
import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ScrollEvent;
import org.zkoss.zul.Slider;

/**
 * The builder of input agent for slider component.
 * This agent only performs onScroll event.
 * @author pao
 */
public class SliderInputAgentBuilder implements OperationAgentBuilder<ComponentAgent, InputAgent> {

	public Class<InputAgent> getOperationClass() {
		return InputAgent.class;
	}

	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentImpl(agent);
	}

	private class InputAgentImpl extends AgentDelegator<ComponentAgent> implements InputAgent {

		public InputAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void input(Object raw) {
			if (raw == null)
				throw new AgentException("value can't be null");

			// parse value
			int value = -1;
			if (raw instanceof Number) {
				if (raw instanceof Double || raw instanceof Float || raw instanceof BigDecimal)
					throw new AgentException("value can't be float type: " + raw.getClass().getName());
				value = ((Number) raw).intValue();
			} else if (raw instanceof String) {
				String text = raw.toString().trim();
				try {
					value = Integer.parseInt(text);
				} catch (NumberFormatException e) {
					throw new AgentException("value must be a valid integer string: " + text, e);
				}
			}
			else
				throw new AgentException("unsupport type of value: " + raw.getClass().getName());
			
			// check bounds
			if(value < 0 || value > target.as(Slider.class).getMaxpos())
				throw new AgentException("value is out of bounds: " + value);
			
			// post AU event
			String cmd = Events.ON_SCROLL;
			ScrollEvent event = new ScrollEvent(cmd, (Component) getDelegatee(), value);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) getClient()).postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
		}

		public void type(String value) {
			throw new AgentException(target + " doesn't support type operation");
		}

		public void typing(String value) {
			throw new AgentException(target + " doesn't support typing operation");
		}

		public void select(int start, int end) {
			throw new AgentException(target + " doesn't support select operation");
		}
	}
}
