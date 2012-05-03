/* SelectionEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectionEvent;

/**
 * @author Hawk
 *
 */
public class SelectionEventDataBuilder implements EventDataBuilder{

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.impl.au.EventDataBuilder#build(org.zkoss.zk.ui.event.Event, java.util.Map)
	 */
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		SelectionEvent evt = (SelectionEvent) event;

		AuUtility.setEssential(data, "start", evt.getStart());
		AuUtility.setEssential(data, "end", evt.getEnd());
		AuUtility.setEssential(data, "selected", evt.getSelectedText());
		return data;
	}

}
