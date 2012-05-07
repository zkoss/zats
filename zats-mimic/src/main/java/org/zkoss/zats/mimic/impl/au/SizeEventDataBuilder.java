/* SizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SizeEvent;

/**
 * The data builder of size event. 
 * @author pao
 */
public class SizeEventDataBuilder implements EventDataBuilder {

	public Map<String, Object> build(Event event, Map<String, Object> data) {
		SizeEvent e = (SizeEvent) event;
		String left = "0px";
		String top = "0px";
		if (e.getTarget() instanceof HtmlBasedComponent) {
			HtmlBasedComponent target = (HtmlBasedComponent) e.getTarget();
			left = target.getLeft();
			top = target.getTop();
		}
		AuUtility.setOptional(data, "left", left);
		AuUtility.setOptional(data, "top", top);
		AuUtility.setEssential(data, "width", e.getWidth());
		AuUtility.setEssential(data, "height", e.getHeight());
		return data;
	}

}
