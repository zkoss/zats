/* CheckEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;

/**
 * A data builder for check event.
 * 
 * @author pao
 */
public class CheckEventDataBuilder implements EventDataBuilder {
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		CheckEvent evt = (CheckEvent) event;
		EventDataManager.setEssential(data, "", evt.isChecked());
		return data;
	}
}
