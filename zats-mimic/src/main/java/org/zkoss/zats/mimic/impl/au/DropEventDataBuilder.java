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
public class DropEventDataBuilder implements EventDataBuilder{

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.impl.au.EventDataBuilder#build(org.zkoss.zk.ui.event.Event, java.util.Map)
	 */
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		DropEvent evt = (DropEvent) event;

		AuUtility.setEssential(data, "dragged", evt.getDragged().getUuid());
		AuUtility.setEssential(data, "pageX", evt.getPageX());
		AuUtility.setEssential(data, "pageY", evt.getPageY());
		AuUtility.setEssential(data, "which", 1);
		AuUtility.setEssential(data, "x", evt.getX());
		AuUtility.setEssential(data, "y", evt.getY());
		return data;
	}

}
