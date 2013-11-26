/* TimeTypeAgentBuilderZK7.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.operation.input.TimeInputAgentBuilder;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * A time type agent builder for ZK7.
 * 
 * @author pao
 */
public class TimeTypeAgentBuilderZK7 extends TimeInputAgentBuilder {
	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentZK6Impl(agent);
	}
	static class InputAgentZK6Impl extends InputAgentImpl{
		
		public InputAgentZK6Impl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				Date time = parseDate(f == null ? DEFAULT_TIME_FORMAT : f, raw.trim());
				data.put("value", "$z!t#d:" + JSONs.d2j(time));
			}
		}
	}
	
	
}
