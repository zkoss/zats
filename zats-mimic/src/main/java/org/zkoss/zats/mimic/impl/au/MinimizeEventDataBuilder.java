/* MinimizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.MinimizeEvent;

/**
 * The data builder of minimize event.
 * @author pao
 */
public class MinimizeEventDataBuilder implements EventDataBuilder<MinimizeEvent> {

	public Map<String, Object> build(MinimizeEvent evt, Map<String, Object> data) {
		AuUtility.setOptional(data, "width", evt.getWidth());
		AuUtility.setOptional(data, "height", evt.getHeight());
		AuUtility.setOptional(data, "left", evt.getLeft());
		AuUtility.setOptional(data, "top", evt.getTop());
		AuUtility.setEssential(data, "minimized", evt.isMinimized());
		return data;
	}
	public Class<MinimizeEvent> getEventClass(){
		return MinimizeEvent.class;
	}
}
