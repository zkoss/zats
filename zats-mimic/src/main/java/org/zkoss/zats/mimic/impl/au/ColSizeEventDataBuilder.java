/* ColSizeEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 7, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.event.ColSizeEvent;

/**
 * The data builder of column size event. 
 * @author pao
 */
public class ColSizeEventDataBuilder implements EventDataBuilder {

	public Map<String, Object> build(Event event, Map<String, Object> data) {
		ColSizeEvent e = (ColSizeEvent) event;
		AuUtility.setEssential(data, "column", e.getColumn().getUuid());
		AuUtility.setEssential(data, "index", e.getColIndex());
		AuUtility.setEssential(data, "width", e.getWidth());
		AuUtility.setEssential(data, "widths", new String[] { e.getWidth() });
		AuUtility.setOptional(data, "which", e.getKeys());
		return data;
	}
}
