/* OpenEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.OpenEvent;

/**
 * AU data builder for OpenEvent.
 * 
 * @author pao
 */
public class OpenEventDataBuilder implements EventDataBuilder<OpenEvent> {
	public Map<String, Object> build(OpenEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "open", evt.isOpen());
		AuUtility.setOptional(data, "value", evt.getValue());
		AuUtility.setReference(data, evt.getReference());
		return data;
	}
	public Class<OpenEvent> getEventClass(){
		return OpenEvent.class;
	}
}