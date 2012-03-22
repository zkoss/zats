/* EventDataManager.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.event.RenderEvent;

/**
 * @author dennis
 *
 */
public class EventDataManager {

	private static Map<Class<? extends Event>, EventDataBuilder> builders;

	static {
		builders = new HashMap<Class<? extends Event>, EventDataBuilder>();
		registerBuilder("*","*", Event.class, new EventDataBuilder(){
			public Map<String, Object> build(Event event,Map<String,Object> data) {
				return data;
			}});
		registerBuilder("*","*", OpenEvent.class, new EventDataBuilder(){
			public Map<String, Object> build(Event event,Map<String,Object> data) {
				OpenEvent evt = (OpenEvent)event;
				setEssential(data,"open",evt.isOpen());
				setOptional(data,"value",evt.getValue());
				setReference(data,evt.getReference());
				return data;
			}});
		registerBuilder("*","*", SelectEvent.class, new EventDataBuilder(){
			public Map<String, Object> build(Event event,Map<String,Object> data) {
				SelectEvent evt = (SelectEvent)event;
				setEssential(data,"items",evt.getSelectedItems());//id set of items
				setReference(data,evt.getReference());
				return data;
			}});
		registerBuilder("*","*", KeyEvent.class, new EventDataBuilder(){
			public Map<String, Object> build(Event event,Map<String,Object> data) {
				KeyEvent evt = (KeyEvent)event;
				
				setEssential(data,"keyCode",evt.getKeyCode());
				setEssential(data,"ctrlKey",evt.isCtrlKey());
				setEssential(data,"shiftKey",evt.isShiftKey());
				setEssential(data,"altKey",evt.isAltKey());
				setReference(data,evt.getReference());
				return data;
			}});
		registerBuilder("*","*", RenderEvent.class, new EventDataBuilder(){
			public Map<String, Object> build(Event event,Map<String,Object> data) {
				RenderEvent evt = (RenderEvent)event;
				
				setEssential(data,"items",evt.getItems());//id set of items
				return data;
			}});
		//more
	}
	
	@SuppressWarnings("unchecked")
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
	
	static private void setEssential(Map<String,Object> data,String key, Object obj){
		if(obj==null) throw new ConversationException("data of "+key+" is null");
		data.put(key, toSafeJsonObject(obj));
	}
	
	static private void setOptional(Map<String,Object> data,String key, Object obj){
		if(obj==null) return;
		data.put(key, toSafeJsonObject(obj));
	}

	static private void setReference(Map<String,Object> data,Component comp){
		if(comp==null) return;
		data.put("reference", comp.getUuid());
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static  
		void registerBuilder(String startVersion, String endVersion, String eventClazz, EventDataBuilder builder) {
		if (startVersion == null || endVersion == null || eventClazz == null || builder == null)
			throw new IllegalArgumentException();
		
		if(!Util.checkVersion(startVersion,endVersion)) return;
		
		Class clz = null;
		try {
			clz = Class.forName(eventClazz);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("compClazz "+eventClazz+" not found ", e);
		}
		if(Event.class.isAssignableFrom(clz)){
			registerBuilder(startVersion,endVersion,clz,builder);
		}else{
			throw new IllegalArgumentException("compClazz "+eventClazz+" is not a component");
		}
	}

	
	public static <T extends Event> 
		void registerBuilder(String startVersion, String endVersion, Class<? extends Event> eventClass, EventDataBuilder builder) {
		
		if (startVersion == null || endVersion == null || eventClass==null || builder == null)
			throw new IllegalArgumentException();

		if(!Util.checkVersion(startVersion,endVersion)) return;
		builders.put(eventClass, builder);
	}

	public static Map<String, Object> build(Event evt) {
		Class<? extends Object> clz = evt.getClass();

		EventDataBuilder builder = null;
		while (clz != null) {
			builder = builders.get(clz);
			if (builder != null)
				break;
			clz = clz.getSuperclass();
			if (!Event.class.isAssignableFrom(clz))
				break;
		}

		if (builder == null) {
			throw new ConversationException("build for event not found : "
					+ evt);
		}

		return builder.build(evt,new HashMap<String,Object>());
	}
}
