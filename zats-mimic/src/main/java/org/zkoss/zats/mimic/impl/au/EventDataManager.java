/* EventDataManager.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.event.RenderEvent;

/**
 * The manager of event data builder.
 * 
 * @author dennis
 */
public class EventDataManager {

	private static Map<Class<? extends Event>, EventDataBuilder> builders;

	static {
		builders = new HashMap<Class<? extends Event>, EventDataBuilder>();
		
		registerBuilder("5.0.0", "*", MouseEvent.class, new MouseEventDataBuilder());
		registerBuilder("5.0.0", "*", InputEvent.class, new InputEventDataBuilder());
		registerBuilder("5.0.0", "*", CheckEvent.class, new CheckEventDataBuilder());
		registerBuilder("5.0.0", "*", Event.class, new DefaultEventDataBuilder());
		registerBuilder("5.0.0", "*", OpenEvent.class, new OpenEventDataBuilder());
		registerBuilder("5.0.0", "*", SelectEvent.class, new SelectEventDataBuilder());
		registerBuilder("5.0.0", "*", KeyEvent.class, new KeyEventDataBuilder());
		registerBuilder("5.0.0", "*", RenderEvent.class, new RenderEventDataBuilder());
		//TODO more
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static  
		void registerBuilder(String startVersion, String endVersion, String eventClazz, String builderClazz) {
		if (startVersion == null || endVersion == null || eventClazz == null || builderClazz == null)
			throw new IllegalArgumentException();
		
		if(!Util.checkVersion(startVersion,endVersion)) return;
		
		Class clz = null;
		try {
			clz = Class.forName(eventClazz);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("eventClazz "+eventClazz+" not found ", e);
		}
		EventDataBuilder builder = null;
		
		try{
			Class buildClz = Class.forName(builderClazz);
			builder = (EventDataBuilder)buildClz.newInstance();
		}catch(Exception x){
			throw new IllegalArgumentException(x.getMessage(),x);
		}
		
		
		if(Event.class.isAssignableFrom(clz)){
			registerBuilder(startVersion,endVersion,clz,builder);
		}else{
			throw new IllegalArgumentException("eventClazz "+eventClazz+" is not a Event");
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
