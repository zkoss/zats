/* DateInputAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 20, 2012 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * Build an InputAgent for Datebox.
 * @author dennis
 *
 */
public class DateInputAgentBuilder extends AbstractInputAgentBuilder {
	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentImpl(agent);
	}
	
	static protected class InputAgentImpl extends AbstractInputAgentImpl{
	
		public InputAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected void putValue(ComponentAgent target,String raw,Map<String , Object> data){
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				Date date = parseDate(f == null ? DEFAULT_DATE_FORMAT : f, raw.trim());
				data.put("value", JSONs.d2j(date));
				data.put("z_type_value", "Date");
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Date){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				return formatDate(f == null ? DEFAULT_DATE_FORMAT : f, ((Date)value));
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
}