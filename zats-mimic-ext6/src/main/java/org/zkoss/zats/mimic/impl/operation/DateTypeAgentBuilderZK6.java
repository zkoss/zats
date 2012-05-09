/* DateTypeAgentBuilderZK6.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * A date type agent builder for ZK6.
 * 
 * @author pao
 */
public class DateTypeAgentBuilderZK6 extends AbstractInputAgentBuilder.DateInputAgentBuilder {
	@Override
	protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
		if(Strings.isBlank(raw)) {
			data.put("value", null);
		}else{
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement) comp).getFormat();
			Date date = parseDate(f == null ? DEFAULT_DATE_FORMAT : f, raw.trim());
			data.put("value", "$z!t#d:" + JSONs.d2j(date));
		}
	}
}
