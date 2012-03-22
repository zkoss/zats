/* AuUtility2.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * @author dennis
 *
 */
public class AuUtility2 {
	public static void postUpdate(ComponentAgent target,Event evt) {
		target.getConversation().postUpdate(target, evt.getName(), EventDataManager.build(evt));
	}
	
	/**
	 * lookup the event target of a component, it look up the component and its ancient. 
	 * use this for search the actual target what will receive a event for a action on a component-agent
	 * <p/>Currently, i get it by server side directly
	 */
	public static ComponentAgent lookupEventTarget(ComponentAgent c,String evtname){
		if(c==null) return null;
		Component comp = c.getComponent();
		if(Events.isListened(comp, evtname, true)){
			return c;
		}
		return lookupEventTarget(c.getParent(),evtname);
		
	}
}
