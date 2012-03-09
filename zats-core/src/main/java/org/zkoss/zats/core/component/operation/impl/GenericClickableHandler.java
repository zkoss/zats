package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.OperationHandler;
import org.zkoss.zats.core.component.operation.OperationManager;
import org.zkoss.zk.ui.Component;

public class GenericClickableHandler implements OperationHandler<Clickable>
{

	public Clickable getOperation(final Component target)
	{
		return new Clickable()
		{
			public Clickable click()
			{
				String uuid = target.getUuid();
				// TODO
				OperationManager.post("onClick", "");
				return this;
			}
		};
	}

}
