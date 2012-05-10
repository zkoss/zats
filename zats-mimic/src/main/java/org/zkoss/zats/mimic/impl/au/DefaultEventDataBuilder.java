/* DefaultEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zk.ui.event.Event;

/**
 * Default builder puts nothing into AU data.
 * 
 * @author pao
 */
public class DefaultEventDataBuilder implements EventDataBuilder<Event> {
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		return data;
	}
	public Class<Event> getEventClass(){
		return Event.class;
	}
}
