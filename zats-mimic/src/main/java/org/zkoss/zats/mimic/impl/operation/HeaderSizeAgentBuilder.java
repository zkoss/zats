/* HeaderSizeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		May 7, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.SizeAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.event.ColSizeEvent;
import org.zkoss.zul.impl.HeaderElement;
import org.zkoss.zul.impl.HeadersElement;

/**
 * The builder for size agent of header element.
 * @author pao
 */
public class HeaderSizeAgentBuilder implements OperationAgentBuilder<ComponentAgent, SizeAgent> {

	public Class<SizeAgent> getOperationClass() {
		return SizeAgent.class;
	}

	public SizeAgent getOperation(ComponentAgent target) {
		// check parent type
		ComponentAgent parent = target.getParent();
		if (parent != null && parent.is(HeadersElement.class))
			return new HeaderSizeAgentImpl(target);
		else
			throw new AgentException(target + " doesn't support resize operation");
	}

	private class HeaderSizeAgentImpl extends AgentDelegator<ComponentAgent> implements SizeAgent {

		public HeaderSizeAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void maximize(boolean maximized) {
			throw new AgentException(target + " doesn't support maximized operation");
		}

		public void minimize(boolean minimized) {
			throw new AgentException(target + " doesn't support minimized operation");
		}

		public void resize(int width, int height) {
			// check width and ignore height
			if (width < 0)
				return; // do nothing

			// get index of column
			HeaderElement column = target.as(HeaderElement.class);
			HeadersElement head = target.getParent().as(HeadersElement.class);
			List<?> children = head.getChildren();
			int index = children.indexOf(column);

			// get widths of columns
			String[] widths = new String[children.size()];
			for (int i = 0; i < widths.length; ++i) {
				widths[i] = ((HeaderElement) children.get(i)).getWidth();
				if (widths[i] == null || widths[i].length() <= 0)
					widths[i] = "200px"; // default width
			}
			// replace new width of target column
			widths[index] = width + "px";

			// check if values are the same
			if (widths[index].equals(column.getWidth()))
				return; // do nothing

			// get inner width of container
			Component container = head.getParent();
			String innerWidth = null;
			try {
				Method method = container.getClass().getMethod("getInnerWidth");
				innerWidth = method.invoke(container).toString();
			} catch (Exception e) {
				new ZatsException("unexceptional exception", e);
			}

			// post onInnerWidth event from container
			ClientCtrl cc = (ClientCtrl) target.getClient();
			String desktopId = target.getDesktop().getId();
			String cmd = "onInnerWidth";
			Map<String, Object> data = EventDataManager.build(new Event(cmd));
			data.put("", innerWidth);
			cc.postUpdate(desktopId, cmd, container.getUuid(), data, null);

			// post column resize event
			cmd = "onColSize";
			ColSizeEvent event = new ColSizeEvent(cmd, head, index, column, widths, 0);
			data = EventDataManager.build(event);
			data.put("widths", widths);
			cc.postUpdate(desktopId, cmd, head.getUuid(), data, null);
		}
	}
}
