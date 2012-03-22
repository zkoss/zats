/* AuUtility.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.event.Events;

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
			BigInteger current = Util.getZKVersion();
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
	
//	/**
//	 * @param item uuid of sub-items to render
//	 */
//	public static void postOnRender(ComponentAgent target, String... item) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		if(item.length==0) return;
//		data.put("items", item);
//		target.getConversation().postUpdate(target, Events.ON_RENDER, data);
//	}
}
