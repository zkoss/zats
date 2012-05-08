/* ScrollEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 8, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.ScrollEvent;

/**
 * The data builder of scroll event.
 * @author pao
 */
public class ScrollEventDataBuilder implements EventDataBuilder<ScrollEvent> {

	public Class<ScrollEvent> getEventClass() {
		return ScrollEvent.class;
	}

	public Map<String, Object> build(ScrollEvent event, Map<String, Object> data) {
		AuUtility.setEssential(data, "", event.getPos());
		return data;
	}
}
