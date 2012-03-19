package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Checkable;
import org.zkoss.zk.ui.event.Events;

public class GenericCheckableBuilder implements OperationBuilder<Checkable>
{

	public Checkable getOperation(final ComponentNode target)
	{
		return new Checkable()
		{
			public Checkable check(final boolean checked)
			{
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("", checked);
				target.getConversation().postUpdate(target, Events.ON_CHECK, data);
				return this;
			}
		};
	}

}
