package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zk.ui.event.Events;

/**
 * Utilities for any related to operation of component.
 * @author pao
 */
public class OperationUtil
{

	private static void doMouseEvent(ComponentNode target, String cmd)
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageX", 0);
		data.put("pageY", 0);
		data.put("x", 0);
		data.put("y", 0);
		if(Events.ON_CLICK.equals(cmd) || Events.ON_DOUBLE_CLICK.equals(cmd))
			data.put("which", 1); // left button
		else if(Events.ON_RIGHT_CLICK.equals(cmd))
			data.put("which", 2); // right button
		target.getConversation().postUpdate(target, cmd, data);
	}

	public static void doClick(ComponentNode target)
	{
		doMouseEvent(target, Events.ON_CLICK);
	}

	public static void doRightClick(ComponentNode target)
	{
		doMouseEvent(target, Events.ON_RIGHT_CLICK);
	}

	public static void doDoubleClick(ComponentNode target)
	{
		doMouseEvent(target, Events.ON_DOUBLE_CLICK);
	}

	public static void doMouseOver(ComponentNode target)
	{
		doMouseEvent(target, Events.ON_MOUSE_OVER);
	}

	public static void doMouseOut(ComponentNode target)
	{
		doMouseEvent(target, Events.ON_MOUSE_OUT);
	}

	private static void doInputEvent(ComponentNode target, Object value, String cmd)
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("value", value);
		data.put("bySelectBack", false);
		data.put("start", 0);
		target.getConversation().postUpdate(target, cmd, data);
	}

	public static void doChange(ComponentNode target, Number value)
	{
		doInputEvent(target, value, Events.ON_CHANGE);
	}

	public static void doChange(ComponentNode target, String value)
	{
		doInputEvent(target, value, Events.ON_CHANGE);
	}

	public static void doChanging(ComponentNode target, Number value)
	{
		doInputEvent(target, value, Events.ON_CHANGING);
	}

	public static void doChanging(ComponentNode target, String value)
	{
		doInputEvent(target, value, Events.ON_CHANGING);
	}

	public static void doFocus(ComponentNode target)
	{
		target.getConversation().postUpdate(target, Events.ON_FOCUS, null);
	}

	public static void doBlur(ComponentNode target)
	{
		target.getConversation().postUpdate(target, Events.ON_BLUR, null);
	}

	public static void doCheck(ComponentNode target, boolean checked)
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("", checked);
		target.getConversation().postUpdate(target, Events.ON_CHECK, data);
	}

	public static void doSelect(ComponentNode target, String selection)
	{
		doSelect(target, selection, selection);
	}

	public static void doSelect(ComponentNode target, String reference, String... selection)
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", selection);
		data.put("reference", reference);
		data.put("which", 0);
		target.getConversation().postUpdate(target, Events.ON_SELECT, data);
	}
}
