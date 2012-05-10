/* SizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 4, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zk.ui.event.SizeEvent;

/**
 * The data builder of size event. 
 * @author pao
 */
public class SizeEventDataBuilder implements EventDataBuilder<SizeEvent>{

	public Map<String, Object> build(SizeEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "width", evt.getWidth());
		AuUtility.setEssential(data, "height", evt.getHeight());
		AuUtility.setEssential(data, "which", evt.getKeys());
		return data;
	}
	public Class<SizeEvent> getEventClass(){
		return SizeEvent.class;
	}
}
