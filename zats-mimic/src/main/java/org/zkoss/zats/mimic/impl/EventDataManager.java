/* EventDataManager.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.impl.au.BookmarkEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.CheckEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.ColSizeEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.DefaultEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.DropEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.InputEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.KeyEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.MaximizeEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.MinimizeEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.MouseEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.MoveEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.OpenEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.PagingEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.RenderEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.ScrollEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.SelectEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.SelectionEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.SizeEventDataBuilder;
import org.zkoss.zats.mimic.impl.au.SortEventDataBuilder;
import org.zkoss.zk.ui.event.Event;

/**
 * The manager of event data builder. <br/>
 * <p>
 * To deal with the issue that different version might sends different AU data for the same event,
 *  we design registration mechanism which is similar with the one for component agent builder. 
 *  We can register different EventDataBuilder for the same event in different version.
 *  </p>
 *  This class maintains a collection of key-value pairs, the key is ZK event, and the value is EventDataBuilder.
 *  EventDataBuilder is responsible for construct AU data content. 
 * @author dennis
 */
public class EventDataManager {

	private static EventDataManager instance;
	
	public static synchronized EventDataManager getInstance(){
		if(instance==null){
			instance = new EventDataManager(); 
		}
		return instance;
	}
	
	private Map<Class<? extends Event>, EventDataBuilder<? extends Event>> builders;

	public EventDataManager() {
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
		registerBuilder("5.0.0", "*", new SortEventDataBuilder());
		registerBuilder("5.0.0", "*", new ScrollEventDataBuilder());
		registerBuilder("5.0.0", "*", new MoveEventDataBuilder());
		//TODO more
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void registerBuilder(String startVersion, String endVersion, String builderClazz) {
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

	
	public <T extends Event> void registerBuilder(String startVersion, String endVersion, 
			EventDataBuilder<? extends Event> builder) {
		
		if (startVersion == null || endVersion == null || builder == null)
			throw new IllegalArgumentException();

		if(!Util.checkVersion(startVersion,endVersion)) return;
		builders.put(builder.getEventClass(), builder);
	}

	public Map<String, Object> build(Event evt) {
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
