/* KeyEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 26, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.KeyEvent;

/**
 * A data builder for KeyEvent.
 * 
 * @author pao
 * 
 */
public class KeyEventDataBuilder implements EventDataBuilder<KeyEvent> {
	public Map<String, Object> build(KeyEvent evt, Map<String, Object> data) {

		AuUtility.setEssential(data, "keyCode", evt.getKeyCode());
		AuUtility.setEssential(data, "ctrlKey", evt.isCtrlKey());
		AuUtility.setEssential(data, "shiftKey", evt.isShiftKey());
		AuUtility.setEssential(data, "altKey", evt.isAltKey());
		AuUtility.setReference(data, evt.getReference());
		return data;
	}
	public Class<KeyEvent> getEventClass(){
		return KeyEvent.class;
	}
}