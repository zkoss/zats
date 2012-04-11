/* AuUtility.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

/**
 * A utility for AU.
 * 
 * @author dennis
 */
public class AuUtility {

	/**
	 * lookup the event target of a component, it look up the component and its
	 * ancient. use this for search the actual target what will receive a event
	 * for a action on a component-agent
	 * <p/>
	 * Currently, i get it by server side directly
	 */
	public static ComponentAgent lookupEventTarget(ComponentAgent c, String evtname) {
		if (c == null)
			return null;
		Component comp = c.getComponent();
		if (Events.isListened(comp, evtname, true)) {
			return c;
		}
		return lookupEventTarget(c.getParent(), evtname);

	}

	static void setEssential(Map<String,Object> data,String key, Object obj){
		if(obj==null) throw new AgentException("data of "+key+" is null");
		data.put(key, toSafeJsonObject(obj));
	}

	static void setOptional(Map<String,Object> data,String key, Object obj){
		if(obj==null) return;
		data.put(key, toSafeJsonObject(obj));
	}

	static void setReference(Map<String,Object> data,Component comp){
		if(comp==null) return;
		data.put("reference", comp.getUuid());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static private Object toSafeJsonObject(Object obj){
		if(obj instanceof Set){
			//exception if data is Set
			//>>Unexpected character (n) at position 10.
			//>>	at org.zkoss.json.parser.Yylex.yylex(Yylex.java:610)
			//>>	at org.zkoss.json.parser.JSONParser.nextToken(JSONParser.java:270)
			return new ArrayList((Set)obj);
		}
		return obj;
	}
}
