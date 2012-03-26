/* RenderEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.event.RenderEvent;

/**
 * A data builder for render event.
 * 
 * @author pao
 */
public class RenderEventDataBuilder implements EventDataBuilder {
	public Map<String, Object> build(Event event,Map<String,Object> data) {
		RenderEvent evt = (RenderEvent)event;
		
		EventDataManager.setEssential(data,"items",evt.getItems());//id set of items
		return data;
	}
}