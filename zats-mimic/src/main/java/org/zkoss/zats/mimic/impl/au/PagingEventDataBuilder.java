/* PagingEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/4 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zul.event.PagingEvent;

/**
 * @author Hawk
 *
 */
public class PagingEventDataBuilder implements EventDataBuilder<PagingEvent> {

	public Map<String, Object> build(PagingEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "", evt.getActivePage());
		return data;
	}
	public Class<PagingEvent> getEventClass(){
		return PagingEvent.class;
	}
}
