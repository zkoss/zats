/* TextInputAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 20, 2012 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.InputAgent;

/**
 * 
 * @author dennis
 *
 */
public class TextInputAgentBuilder extends AbstractInputAgentBuilder {
	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentImpl(agent);
	}
	
	static class InputAgentImpl extends AbstractInputAgentImpl{
	
		public InputAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			data.put("value", raw);
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			return value==null?"":value.toString();
		}
	}
}