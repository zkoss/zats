/* SelectionEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/3 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zats.mimic.impl.EventDataBuilder;
import org.zkoss.zk.ui.event.SelectionEvent;

/**
 * @author Hawk
 *
 */
public class SelectionEventDataBuilder implements EventDataBuilder<SelectionEvent>{

	public Map<String, Object> build(SelectionEvent evt, Map<String, Object> data) {

		AuUtility.setEssential(data, "start", evt.getStart());
		AuUtility.setEssential(data, "end", evt.getEnd());
		AuUtility.setEssential(data, "selected", evt.getSelectedText());
		return data;
	}
	public Class<SelectionEvent> getEventClass(){
		return SelectionEvent.class;
	}
}
