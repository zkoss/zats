package org.zkoss.zats.core.component.operation;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSONPojoConvertorFactory;
import org.zkoss.zk.ui.Component;

public class OperationManager
{
	static
	{
		// TODO load default implement
		// TODO load custom implement from configuration
	}

	public static void addMapping(Class<Component> component, Class<Operation> operation, OperationHandler handler)
	{
	}

	public static OperationHandler getHandler(Component component, Operation operation)
	{
		
		return null;
	}
}
