/* ColorInputAgentBuilder.java

	Purpose:

	Description:

	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.AgentDelegator;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zk.ui.event.Events;

/**
 * A specific {@link InputAgent} implementation for Colorbox, because its AU data only contains 1 key-value pair, {"color":"#333366"}
 * @author Hawk
 *
 */
public class ColorboxInputAgentBuilder implements OperationAgentBuilder<ComponentAgent,InputAgent>{
	public InputAgent getOperation(final ComponentAgent target) {
		return new TypeAgentImpl(target);
	}
	
	public Class<InputAgent> getOperationClass() {
		return InputAgent.class;
	}

	class TypeAgentImpl extends AgentDelegator<ComponentAgent> implements InputAgent{

		/**
		 * @param target
		 */
		public TypeAgentImpl(ComponentAgent target) {
			super(target);
		}
		
		public void input(Object value){
			type(toRawString(target,value));
		}
		
		protected String toRawString(ComponentAgent target, Object value){
			return value==null?"":value.toString();
		}

		public void type(String value) {
			final String COLOR_PATTERN = "^#([a-fA-F0-9]{6})";
			//check input format first
			if (value.matches(COLOR_PATTERN)){
				String cmd = Events.ON_CHANGE;
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("color",value); // parse value and put into data collection
				String desktopId = target.getDesktop().getId();
				((ClientCtrl) target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
				((ClientCtrl) getClient()).flush(desktopId);
			}else{
				throw new AgentException("value \"" + value
						+ "\"is invalid for the component: "
						+ target);
			}

		}

		public void typing(String value) {
			throw new AgentException("Unsuppported operation");

		}

		public void select(int start, int end) {
			throw new AgentException("Unsuppported operation");
		}
		
	}

}
