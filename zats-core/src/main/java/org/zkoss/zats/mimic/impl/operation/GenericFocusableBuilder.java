package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Focusable;

public class GenericFocusableBuilder implements OperationBuilder<Focusable>
{
	public Focusable getOperation(final ComponentNode target)
	{
		return new Focusable()
		{
			public Focusable focus()
			{
				OperationUtil.doFocus(target);
				return this;
			}

			public Focusable blur()
			{
				OperationUtil.doBlur(target);
				return this;
			}
		};
	}

}
