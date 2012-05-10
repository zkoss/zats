/* CheckEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zk.ui.event.CheckEvent;

/**
 * A data builder for CheckEvent.
 * 
 * @author pao
 */
public class CheckEventDataBuilder implements EventDataBuilder<CheckEvent> {
	public Map<String, Object> build(CheckEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "", evt.isChecked());
		return data;
	}
	public Class<CheckEvent> getEventClass(){
		return CheckEvent.class;
	}
}
