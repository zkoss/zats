package org.zkoss.zats.core.component.operation.impl;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.OperationBuilder;
import org.zkoss.zats.core.component.operation.OperationManager;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

public class GenericClickableBuilder implements OperationBuilder<Clickable>
{
	public Clickable getOperation(final Component target)
	{
		return new Clickable()
		{
			public Clickable click()
			{
				String name = Events.ON_CLICK;
				String uuid = target.getUuid();
				Map<String, Object> data = new HashMap<String, Object>();
				OperationManager.post(target, "onClick", data);
				return this;
			}
		};
	}
}
