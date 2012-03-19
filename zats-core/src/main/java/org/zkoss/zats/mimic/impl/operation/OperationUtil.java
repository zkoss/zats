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
	
	public static void doChange(ComponentNode target , String value)
	{
		
	}
	
}
