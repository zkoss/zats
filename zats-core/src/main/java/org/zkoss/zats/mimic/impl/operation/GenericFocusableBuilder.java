package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Focusable;
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
