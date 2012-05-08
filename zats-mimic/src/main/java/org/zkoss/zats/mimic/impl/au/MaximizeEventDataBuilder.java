/* MaximizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.MaximizeEvent;

/**
 * The data builder of maximize event.
 * @author pao
 */
public class MaximizeEventDataBuilder implements EventDataBuilder<MaximizeEvent> {

	public Map<String, Object> build(MaximizeEvent evt, Map<String, Object> data) {
		AuUtility.setOptional(data, "width", evt.getWidth());
		AuUtility.setOptional(data, "height", evt.getHeight());
		AuUtility.setOptional(data, "left", evt.getLeft());
		AuUtility.setOptional(data, "top", evt.getTop());
		AuUtility.setEssential(data, "maximized", evt.isMaximized());
		return data;
	}
	public Class<MaximizeEvent> getEventClass(){
		return MaximizeEvent.class;
	}
}
