/* SelectEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;

/**
 * A data builder for select event.
 * 
 * @author pao
 */
public class SelectEventDataBuilder implements EventDataBuilder {
	public Map<String, Object> build(Event event,Map<String,Object> data) {
		SelectEvent evt = (SelectEvent)event;
		AuUtility.setEssential(data,"items",evt.getSelectedItems());//id set of items
		AuUtility.setReference(data,evt.getReference());
		return data;
	}
}