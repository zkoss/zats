package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Clickable;
import org.zkoss.zk.ui.event.Events;

public class GenericClickableBuilder implements OperationBuilder<Clickable>
{
	public Clickable getOperation(final ComponentNode target)
	{
		return new Clickable()
		{
			public Clickable click()
			{
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("which", 1); // left button
				data.put("pageX", 0);
				data.put("pageY", 0);
				data.put("x", 0);
				data.put("y", 0);
				target.getConversation().postUpdate(target, Events.ON_CLICK, data);
				return this;
			}
		};
	}
}
