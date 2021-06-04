/* TimeTypeAgentBuilderZK96.java

	Purpose:
		
	Description:
		
	History:
		Fri Jun 4 12:46:50 CST 2021, Created by jameschu

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONs;
import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * A time type agent builder for ZK 9.6
 * 
 * @author jameschu
 */
public class TimeTypeAgentBuilderZK96 extends TimeInputAgentBuilder {
	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentZK6Impl(agent);
	}

	static class InputAgentZK6Impl extends InputAgentImpl {

		public InputAgentZK6Impl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if (Strings.isBlank(raw)) {
				data.put("value", null);
			} else {
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				Date time = parseDate(f == null ? DEFAULT_TIME_FORMAT : f, raw.trim());
				data.put("value", JSONs.d2j(time));
				JSONArray jsonArray = new JSONArray();
				jsonArray.add("value");
				data.put("z$dateKeys", jsonArray);
			}
		}
	}

}
