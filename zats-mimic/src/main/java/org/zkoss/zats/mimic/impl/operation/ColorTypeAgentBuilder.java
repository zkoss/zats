/* ColorTypeAgentBuilder.java

	Purpose:

	Description:

	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.ui.event.Events;

/**
 * A specific TypeAgent implementation for colorbox, because its AU data only contains 1 key-value pair, {"color":"#333366"}
 * @author Hawk
 *
 */
public class ColorTypeAgentBuilder implements OperationAgentBuilder<ComponentAgent,TypeAgent>{
	public TypeAgent getOperation(final ComponentAgent target) {
		return new TypeAgentImpl(target);
	}
	
	public Class<TypeAgent> getOperationClass() {
		return TypeAgent.class;
	}

	class TypeAgentImpl extends AgentDelegator<ComponentAgent> implements TypeAgent{

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

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.TypeAgent#type(java.lang.String)
		 */
		public void type(String value) {
			final String COLOR_PATTERN = "^#([a-fA-F0-9]{6})";
			//check input format first
			if (value.matches(COLOR_PATTERN)){
				String cmd = Events.ON_CHANGE;
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("color",value); // parse value and put into data collection 
				((ClientCtrl) target.getClient()).postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
			}else{
				throw new AgentException("value \"" + value
						+ "\"is invalid for the component: "
						+ target);
			}

		}

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.TypeAgent#typing(java.lang.String)
		 */
		public void typing(String value) {
			throw new AgentException("Unsuppported operation");

		}

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.TypeAgent#select(int, int)
		 */
		public void select(int start, int end) {
			throw new AgentException("Unsuppported operation");
			
		}
		
		

	}

}
