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

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.event.Event;

/**
 * The manager of event data builder. <br/>
 * 
 * To deal with the issue that different version might contains different data content for the same event,
 *  we design registration mechanism. We can register different Event Data Builder for the same event 
 *  in different version.
 * @author dennis
 */
public class EventDataManager {

	private static Map<Class<? extends Event>, EventDataBuilder<? extends Event>> builders;

	static {
		builders = new HashMap<Class<? extends Event>, EventDataBuilder<? extends Event>>();
		
		registerBuilder("5.0.0", "*", new MouseEventDataBuilder());
		registerBuilder("5.0.0", "*", new InputEventDataBuilder());
		registerBuilder("5.0.0", "*", new CheckEventDataBuilder());
		registerBuilder("5.0.0", "*", new DefaultEventDataBuilder());
		registerBuilder("5.0.0", "*", new OpenEventDataBuilder());
		registerBuilder("5.0.0", "*", new SelectEventDataBuilder());
		registerBuilder("5.0.0", "*", new KeyEventDataBuilder());
		registerBuilder("5.0.0", "*", new RenderEventDataBuilder());
		registerBuilder("5.0.0", "*", new MaximizeEventDataBuilder());
		registerBuilder("5.0.0", "*", new MinimizeEventDataBuilder());
		registerBuilder("5.0.0", "*", new DropEventDataBuilder());
		registerBuilder("5.0.0", "*", new SelectionEventDataBuilder());
		registerBuilder("5.0.0", "*", new SizeEventDataBuilder());
		registerBuilder("5.0.0", "*", new PagingEventDataBuilder());
		registerBuilder("5.0.0", "*", new BookmarkEventDataBuilder());
		registerBuilder("5.0.0", "*", new ColSizeEventDataBuilder());
		registerBuilder("5.0.0", "*", new ScrollEventDataBuilder());
		//TODO more
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static  
		void registerBuilder(String startVersion, String endVersion, String builderClazz) {
		if (startVersion == null || endVersion == null || builderClazz == null)
			throw new IllegalArgumentException();
		
		if(!Util.checkVersion(startVersion,endVersion)) return;
		
		EventDataBuilder builder = null;
		try{
			Class buildClz = Class.forName(builderClazz);
			builder = (EventDataBuilder)buildClz.newInstance();
		}catch(Exception x){
			throw new IllegalArgumentException(x.getMessage(),x);
		}
		
		registerBuilder(startVersion,endVersion,builder);
	}

	
	public static <T extends Event> 
		void registerBuilder(String startVersion, String endVersion, EventDataBuilder<? extends Event> builder) {
		
		if (startVersion == null || endVersion == null || builder == null)
			throw new IllegalArgumentException();

		if(!Util.checkVersion(startVersion,endVersion)) return;
		builders.put(builder.getEventClass(), builder);
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
			throw new AgentException("build for event not found : "
					+ evt);
		}

		return builder.build(evt,new HashMap<String,Object>());
	}
}
