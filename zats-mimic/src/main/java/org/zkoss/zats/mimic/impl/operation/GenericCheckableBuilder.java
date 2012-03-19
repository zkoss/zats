package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Checkable;

public class GenericCheckableBuilder implements OperationBuilder<Checkable>
{

	public Checkable getOperation(final ComponentNode target)
	{
		return new Checkable()
		{
			public Checkable check(boolean checked)
			{
				OperationUtil.doCheck(target, checked);
				return this;
			}
		};
	}

}
