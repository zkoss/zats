/* RenderEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zul.event.RenderEvent;

/**
 * AU data builder for RenderEvent.
 * 
 * @author pao
 */
public class RenderEventDataBuilder implements EventDataBuilder<RenderEvent> {
	public Map<String, Object> build(RenderEvent evt,Map<String,Object> data) {
		
		AuUtility.setEssential(data,"items",evt.getItems());//id set of items
		return data;
	}
	public Class<RenderEvent> getEventClass(){
		return RenderEvent.class;
	}
}
