package org.zkoss.zats.mimic.impl.operation;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Typeable;
import org.zkoss.zul.impl.InputElement;

public class GenericTypeableBuilder implements OperationBuilder<Typeable>
{
	public final static int TEXT = 0;
	public final static int INTEGER = 1;
	public final static int DECIMAL = 2;
	private int type;

	public GenericTypeableBuilder(int type)
	{
		if(type < 0 || type > 2)
			throw new IllegalArgumentException("unknown type: " + type);
		this.type = type;
	}

	public Typeable getOperation(final ComponentNode target)
	{
		return new Typeable()
		{
			public Typeable type(String value)
			{
				try
				{
					// parse value
					Object parsed = null;
					if(type == TEXT)
						parsed = value;
					else if(type == INTEGER)
						parsed = new BigInteger(value);
					else if(type == DECIMAL)
						parsed = new BigDecimal(value);

					// validate parsed value with constraints
					InputElement input = target.cast(InputElement.class);
					Method method = InputElement.class.getDeclaredMethod("validate", Object.class);
					method.invoke(input, parsed);

					// TODO onBlur and onFocus
					OperationUtil.doChanging(target, value);
					OperationUtil.doChange(target, value);
				}
				catch(Exception e)
				{
					throw new ConversationException("value \"" + value + "\"is invalid for the component: " + target.getType(), e);
				}
				return this;
			}
		};
	}
}
