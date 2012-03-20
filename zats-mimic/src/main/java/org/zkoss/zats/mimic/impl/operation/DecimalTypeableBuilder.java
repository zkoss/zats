package org.zkoss.zats.mimic.impl.operation;

import java.math.BigDecimal;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Typeable;

public class DecimalTypeableBuilder implements OperationBuilder<Typeable>
{
	public Typeable getOperation(final ComponentNode target)
	{
		return new Typeable()
		{
			public Typeable type(String value)
			{
				try
				{
					BigDecimal decimal = new BigDecimal(value);
					// TODO onBlur and onFocus
					OperationUtil.doChanging(target, decimal);
					OperationUtil.doChange(target, decimal);
				}
				catch(Exception e)
				{
					throw new ConversationException("value should be decimal", e);
				}
				return this;
			}
		};
	}
}
