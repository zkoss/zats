/* ColSizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 7, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zul.event.ColSizeEvent;

/**
 * The data builder for ColSizeEvent. 
 * @author pao
 */
public class ColSizeEventDataBuilder implements EventDataBuilder<ColSizeEvent> {

	public Map<String, Object> build(ColSizeEvent e, Map<String, Object> data) {
		AuUtility.setEssential(data, "column", e.getColumn().getUuid());
		AuUtility.setEssential(data, "index", e.getColIndex());
		AuUtility.setEssential(data, "width", e.getWidth());
		AuUtility.setEssential(data, "widths", new String[] { e.getWidth() });
		AuUtility.setOptional(data, "which", e.getKeys());
		return data;
	}

	public Class<ColSizeEvent> getEventClass() {
		return ColSizeEvent.class;
	}
}
