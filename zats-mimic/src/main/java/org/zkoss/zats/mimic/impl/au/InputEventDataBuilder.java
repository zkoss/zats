/* InputEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * A data builder for input event.
 * 
 * @author pao
 */
public class InputEventDataBuilder implements EventDataBuilder {
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		InputEvent evt = (InputEvent) event;
		AuUtility.setEssential(data, "value", evt.getValue());
		AuUtility.setEssential(data, "start", evt.getStart());
		AuUtility.setEssential(data, "bySelectBack", evt.isChangingBySelectBack());
		return data;
	}
}
