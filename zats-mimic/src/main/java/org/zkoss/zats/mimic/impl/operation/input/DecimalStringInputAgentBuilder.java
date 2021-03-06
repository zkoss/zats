/* DecimalStringInputAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 20, 2012 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.math.BigDecimal;
import java.util.Map;

import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * Build the InputAgent object for ZK components that accept Double input. 
 * @author dennis
 *
 */
public class DecimalStringInputAgentBuilder extends AbstractInputAgentBuilder {
	public InputAgent getOperation(ComponentAgent agent) {
		return new InputAgentImpl(agent);
	}
	
	static class InputAgentImpl extends AbstractInputAgentImpl{
	
		public InputAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					data.put("value", parseNumber(f, raw.trim()).toString());
				else
					data.put("value", new BigDecimal(raw.trim()).toString()); // decimalbox
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Number){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					return formatNumber(f, (Number)value);
				return value.toString();
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
}