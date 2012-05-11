/* EventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;

/**
 * @author dennis
 *
 */
public interface EventDataBuilder<E extends Event> {

	Map<String,Object> build(E event,Map<String,Object> data);
	
	Class<E> getEventClass();
}
