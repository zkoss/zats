/* MaximizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MaximizeEvent;

/**
 * The data builder of maximize event.
 * @author pao
 */
public class MaximizeEventDataBuilder implements EventDataBuilder {

	public Map<String, Object> build(Event event, Map<String, Object> data) {
		MaximizeEvent e = (MaximizeEvent) event;
		AuUtility.setOptional(data, "width", e.getWidth());
		AuUtility.setOptional(data, "height", e.getHeight());
		AuUtility.setOptional(data, "left", e.getLeft());
		AuUtility.setOptional(data, "top", e.getTop());
		AuUtility.setEssential(data, "maximized", e.isMaximized());
		return data;
	}
}
