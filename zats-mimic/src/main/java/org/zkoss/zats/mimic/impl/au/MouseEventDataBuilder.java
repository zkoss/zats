/* MouseEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;

/**
 * A data builder for mouse event.
 * 
 * @author pao
 */
public class MouseEventDataBuilder implements EventDataBuilder<MouseEvent> {
	public Map<String, Object> build(MouseEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "pageX", evt.getPageX());
		AuUtility.setEssential(data, "pageY", evt.getPageY());
		AuUtility.setEssential(data, "x", evt.getX());
		AuUtility.setEssential(data, "y", evt.getY());
		AuUtility.setOptional(data, "area", evt.getArea());
		// parse key
		int keys = evt.getKeys();
		if (keys == 0) {
			// generate data by command name
			String cmd = evt.getName();
			if (Events.ON_CLICK.equals(cmd) || Events.ON_DOUBLE_CLICK.equals(cmd))
				AuUtility.setEssential(data, "which", 1); // left button
			else if (Events.ON_RIGHT_CLICK.equals(cmd))
				AuUtility.setEssential(data, "which", 2); // right button
		} else {
			// generate data by parsing keys
			if ((keys & MouseEvent.LEFT_CLICK) > 0)
				AuUtility.setEssential(data, "which", 1);
			if ((keys & MouseEvent.MIDDLE_CLICK) > 0)
				AuUtility.setEssential(data, "which", 2);
			if ((keys & MouseEvent.RIGHT_CLICK) > 0)
				AuUtility.setEssential(data, "which", 3);
			if ((keys & MouseEvent.ALT_KEY) > 0)
				AuUtility.setEssential(data, "altKey", true);
			if ((keys & MouseEvent.CTRL_KEY) > 0)
				AuUtility.setEssential(data, "ctrlKey", true);
			if ((keys & MouseEvent.SHIFT_KEY) > 0)
				AuUtility.setEssential(data, "shiftKey", true);
		}
		return data;
	}
	public Class<MouseEvent> getEventClass(){
		return MouseEvent.class;
	}
}
