/* MoveEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 8, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.MoveEvent;

/**
 * The data builder of MoveEvent.
 * @author pao
 */
public class MoveEventDataBuilder implements EventDataBuilder<MoveEvent> {

	public Class<MoveEvent> getEventClass() {
		return MoveEvent.class;
	}

	public Map<String, Object> build(MoveEvent event, Map<String, Object> data) {
		AuUtility.setEssential(data, "left", event.getLeft());
		AuUtility.setEssential(data, "top", event.getTop());
		AuUtility.setEssential(data, "which", event.getKeys());
		return data;
	}
}
