package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.component.operation.impl.GenericClickableHandler;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

public class OperationManager
{
	static
	{
		// TODO load default implement
		setMapping(Button.class, new GenericClickableHandler());
		setMapping(Label.class, new GenericClickableHandler());
		// TODO load custom implement from configuration
	}

	public static <T extends Operation, C extends Component> void setMapping(Class<C> component, OperationHandler<T> handler)
	{

	}

	public static <T extends Operation, C extends Component> OperationHandler<T> getHandler(Class<C> component, Class<T> operation)
	{
		return null;
	}

	public static void post(String cmd, String jsonData)
	{

	}

	public static void addObserver(String cmd, OperationObserver observer)
	{

	}
}
