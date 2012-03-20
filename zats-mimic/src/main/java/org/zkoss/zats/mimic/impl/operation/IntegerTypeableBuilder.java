package org.zkoss.zats.mimic.impl.operation;

import java.math.BigInteger;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Typeable;

public class IntegerTypeableBuilder implements OperationBuilder<Typeable>
{
	public Typeable getOperation(final ComponentNode target)
	{
		return new Typeable()
		{
			public Typeable type(String value)
			{
				try
				{
					BigInteger integer = new BigInteger(value);
					// TODO onBlur and onFocus
					OperationUtil.doChanging(target, integer);
					OperationUtil.doChange(target, integer);
				}
				catch(Exception e)
				{
					throw new ConversationException("value should be integer", e);
				}
				return this;
			}
		};
	}
}
