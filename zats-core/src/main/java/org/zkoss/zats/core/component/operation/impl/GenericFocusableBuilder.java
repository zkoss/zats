package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Focusable;
import org.zkoss.zk.ui.event.Events;

public class GenericFocusableBuilder implements OperationBuilder<Focusable>
{
	public Focusable getOperation(final ComponentNode target)
	{
		return new Focusable()
		{
			public Focusable focus()
			{
				target.getConversation().postUpdate(target, Events.ON_FOCUS, null);
				return this;
			}

			public Focusable blur()
			{
				target.getConversation().postUpdate(target, Events.ON_BLUR, null);
				return this;
			}
		};
	}

}
