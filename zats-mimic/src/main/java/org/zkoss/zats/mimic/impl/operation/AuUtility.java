/* AuUtility.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * An agent for delegating task of posting asynchronous update event.
 * 
 * @author pao
 */
public class AuUtility {

	private static void postMouseEvent(ComponentAgent target, String cmd) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageX", 0);
		data.put("pageY", 0);
		data.put("x", 0);
		data.put("y", 0);
		if (Events.ON_CLICK.equals(cmd) || Events.ON_DOUBLE_CLICK.equals(cmd))
			data.put("which", 1); // left button
		else if (Events.ON_RIGHT_CLICK.equals(cmd))
			data.put("which", 2); // right button
		target.getConversation().postUpdate(target, cmd, data);
	}

	public static void postOnClick(ComponentAgent target) {
		postMouseEvent(target, Events.ON_CLICK);
	}

	public static void postOnRightClick(ComponentAgent target) {
		postMouseEvent(target, Events.ON_RIGHT_CLICK);
	}

	public static void postOnDoubleClick(ComponentAgent target) {
		postMouseEvent(target, Events.ON_DOUBLE_CLICK);
	}

	public static void postOnMouseOver(ComponentAgent target) {
		postMouseEvent(target, Events.ON_MOUSE_OVER);
	}

	public static void postOnMouseOut(ComponentAgent target) {
		postMouseEvent(target, Events.ON_MOUSE_OUT);
	}

	private static void postInputEvent(ComponentAgent target, Object value,
			String cmd) {
		Map<String, Object> data = new HashMap<String, Object>();
		// date format is different between ZK5 and ZK6
		if (value instanceof Date) {
			BigInteger current = OperationAgentManager.getZKCurrentVersion();
			if (current.compareTo(Util.parseVersion("6.0.0")) < 0) {
				// zk5
				value = JSONs.d2j((Date) value);
				data.put("z_type_value", "Date");
			} else
				value = "$z!t#d:" + JSONs.d2j((Date) value); // zk6
		}
		data.put("value", value);
		data.put("bySelectBack", false);
		data.put("start", 0);
		target.getConversation().postUpdate(target, cmd, data);
	}

	public static void postOnChange(ComponentAgent target, Object value) {
		postInputEvent(target, value, Events.ON_CHANGE);
	}

	public static void postOnChanging(ComponentAgent target, String value) {
		postInputEvent(target, value, Events.ON_CHANGING);
	}

	public static void postOnFocus(ComponentAgent target) {
		target.getConversation().postUpdate(target, Events.ON_FOCUS, null);
	}

	public static void postOnBlur(ComponentAgent target) {
		target.getConversation().postUpdate(target, Events.ON_BLUR, null);
	}

	public static void postOnCheck(ComponentAgent target, boolean checked) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("", checked);
		target.getConversation().postUpdate(target, Events.ON_CHECK, data);
	}

	public static void postOnSelect(ComponentAgent target, String selection) {
		postOnSelect(target, selection, selection);
	}

	public static void postOnSelect(ComponentAgent target, String reference,
			String... selection) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", selection);
		data.put("reference", reference);
		data.put("which", 0);
		target.getConversation().postUpdate(target, Events.ON_SELECT, data);
	}
	
	public static void postOnRender(ComponentAgent target, String... item) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(item.length==0) return;
		data.put("items", item);
		target.getConversation().postUpdate(target, Events.ON_RENDER, data);
	}
	/**
	 * @param target the component that listen the event
	 * @param command {@link Events#ON_OK}, {@link Events#ON_CANCE} or {@link Events#ON_CTRL_KEY}
	 * @param referene the component that trigger the key 
	 */
	public static void postKeyEvent(ComponentAgent target, String command, ComponentAgent referene) {
		postKeyEvent(target,command,0,false,false,false,null);
	}
	/**
	 * @param command {@link Events#ON_OK}, {@link Events#ON_CANCE} or {@link Events#ON_CTRL_KEY}
	 * @param referene the component that trigger the key
	 */
	public static void postKeyEvent(ComponentAgent target, String command,int keyCode,
			boolean ctrlKey, boolean shiftKey, boolean altKey, ComponentAgent reference) {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("keyCode", keyCode);
		data.put("ctrlKey", ctrlKey);
		data.put("shiftKey", shiftKey);
		data.put("altKey", altKey);
		data.put("reference", target.getUuid());
		
		target.getConversation().postUpdate(target, command, data);
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
