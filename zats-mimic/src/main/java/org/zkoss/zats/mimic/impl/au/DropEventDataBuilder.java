/* DropEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/2 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;

/**
 * @author Hawk
 *
 */
public class DropEventDataBuilder implements EventDataBuilder<DropEvent>{

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.impl.au.EventDataBuilder#build(org.zkoss.zk.ui.event.Event, java.util.Map)
	 */
	public Map<String, Object> build(DropEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "dragged", evt.getDragged().getUuid());
		AuUtility.setEssential(data, "pageX", evt.getPageX());
		AuUtility.setEssential(data, "pageY", evt.getPageY());
		AuUtility.setEssential(data, "which", 1);
		AuUtility.setEssential(data, "x", evt.getX());
		AuUtility.setEssential(data, "y", evt.getY());
		return data;
	}
	public Class<DropEvent> getEventClass(){
		return DropEvent.class;
	}

}
