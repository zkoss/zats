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
import org.zkoss.zk.ui.event.SizeEvent;

/**
 * The data builder of size event. 
 * @author pao
 */
public class SizeEventDataBuilder implements EventDataBuilder<SizeEvent>{

	public Map<String, Object> build(SizeEvent evt, Map<String, Object> data) {
		String left = "0px";
		String top = "0px";
		if (evt.getTarget() instanceof HtmlBasedComponent) {
			HtmlBasedComponent target = (HtmlBasedComponent) evt.getTarget();
			left = target.getLeft();
			top = target.getTop();
		}
		AuUtility.setOptional(data, "left", left);
		AuUtility.setOptional(data, "top", top);
		AuUtility.setEssential(data, "width", evt.getWidth());
		AuUtility.setEssential(data, "height", evt.getHeight());
		return data;
	}
	public Class<SizeEvent> getEventClass(){
		return SizeEvent.class;
	}
}
