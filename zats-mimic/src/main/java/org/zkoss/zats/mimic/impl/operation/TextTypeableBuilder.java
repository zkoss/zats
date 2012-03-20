package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Typeable;

public class TextTypeableBuilder implements OperationBuilder<Typeable>
{
	public Typeable getOperation(final ComponentNode target)
	{
		return new Typeable()
		{
			public Typeable type(String value)
			{
				// TODO onBlur and onFocus
				OperationUtil.doChanging(target, value);
				OperationUtil.doChange(target, value);
				return this;
			}
		};
	}
}
