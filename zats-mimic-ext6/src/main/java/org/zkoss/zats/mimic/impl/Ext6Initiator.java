/* Initial.java

	Purpose:
		
	Description:
		
	History:
		2012/3/23 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.impl.au.EventDataBuilder;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationAgentManager;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.WebAppInit;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.event.RenderEvent;

/**
 * @author dennis
 *
 */
public class Ext6Initiator implements WebAppInit{

	public void init(WebApp wapp) throws Exception {
		
		//testcases and mimic server is in the same vm. 
		//so it is ok to register builder by webapp init
		
//		EventDataManager.registerBuilder("6.0.0","*", RenderEvent.class, new EventDataBuilder(){
//			public Map<String, Object> build(Event event,Map<String,Object> data) {
//				RenderEvent evt = (RenderEvent)event;
//				setEssential(data,"items",evt.getItems());//id set of items
//				return data;
//			}});
		
		OperationAgentManager.registerBuilder("6.0.0", "*", Toolbarbutton.class, CheckAgent.class, 
				new GenericCheckAgentBuilder());
	}
//	
//	@SuppressWarnings("unchecked")
//	static private Object toSafeJsonObject(Object obj){
//		if(obj instanceof Set){
//			//exception if data is Set
//			//>>Unexpected character (n) at position 10.
//			//>>	at org.zkoss.json.parser.Yylex.yylex(Yylex.java:610)
//			//>>	at org.zkoss.json.parser.JSONParser.nextToken(JSONParser.java:270)
//			return new ArrayList((Set)obj);
//		}
//		return obj;
//	}
//	
//	static private void setEssential(Map<String,Object> data,String key, Object obj){
//		if(obj==null) throw new ConversationException("data of "+key+" is null");
//		data.put(key, toSafeJsonObject(obj));
//	}
//	
//	static private void setOptional(Map<String,Object> data,String key, Object obj){
//		if(obj==null) return;
//		data.put(key, toSafeJsonObject(obj));
//	}
//
//	static private void setReference(Map<String,Object> data,Component comp){
//		if(comp==null) return;
//		data.put("reference", comp.getUuid());
//	}

}
