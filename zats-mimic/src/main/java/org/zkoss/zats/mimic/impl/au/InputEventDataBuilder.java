/* InputEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * A data builder for InputEvent.
 * 
 * @author pao
 */
public class InputEventDataBuilder implements EventDataBuilder<InputEvent> {
	public Map<String, Object> build(InputEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "value", evt.getValue(), true);
		AuUtility.setEssential(data, "start", evt.getStart());
		AuUtility.setEssential(data, "bySelectBack", evt.isChangingBySelectBack());
		return data;
	}
	public Class<InputEvent> getEventClass(){
		return InputEvent.class;
	}
}
