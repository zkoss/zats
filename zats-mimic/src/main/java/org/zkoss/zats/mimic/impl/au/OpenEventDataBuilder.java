/* OpenEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.OpenEvent;

/**
 * A data builder for open event.
 * 
 * @author pao
 */
public class OpenEventDataBuilder implements EventDataBuilder {
	public Map<String, Object> build(Event event, Map<String, Object> data) {
		OpenEvent evt = (OpenEvent) event;
		AuUtility.setEssential(data, "open", evt.isOpen());
		AuUtility.setOptional(data, "value", evt.getValue());
		AuUtility.setReference(data, evt.getReference());
		return data;
	}
}