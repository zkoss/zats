/* SortEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SortEvent;

/**
 * @author Hawk
 *
 */
public class SortEventDataBuilder implements EventDataBuilder {

	public Map<String, Object> build(Event event, Map<String, Object> data) {
		SortEvent evt = (SortEvent) event;
		AuUtility.setEssential(data, "", evt.isAscending());
		return data;
	}

}
