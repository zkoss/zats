package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.Operation;
import org.zkoss.zats.core.component.operation.OperationHandler;

public class GenericClickableHandler implements OperationHandler, Clickable
{

	public Clickable click()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends Operation> T as(Class<T> c)
	{
		return (T)this;
	}
}
