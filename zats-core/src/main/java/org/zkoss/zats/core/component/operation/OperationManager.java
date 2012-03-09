package org.zkoss.zats.core.component.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.impl.GenericClickableBuilder;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

public class OperationManager
{
	private static Logger logger;
	private static List<OperationObserver> observers;
	private static Map<Key, OperationBuilder<? extends Operation>> builders;

	static
	{
		logger = Logger.getLogger(OperationObserver.class.getName());
		observers = new CopyOnWriteArrayList<OperationObserver>();
		builders = new HashMap<OperationManager.Key, OperationBuilder<? extends Operation>>();
		// TODO load default implement
		registerBuilder(Button.class, Clickable.class, new GenericClickableBuilder());
		registerBuilder(Label.class, Clickable.class, new GenericClickableBuilder());
		// TODO searching parent builder
		registerBuilder(AbstractComponent.class, Clickable.class, new GenericClickableBuilder());
		// TODO load custom implement from configuration
	}

	public static <T extends Operation, C extends Component> void registerBuilder(Class<C> component, Class<T> operation, OperationBuilder<T> handler)
	{
		if(component == null || operation == null || handler == null)
			return;
		builders.put(new Key(component, operation), handler);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Operation, C extends Component> OperationBuilder<T> getBuilder(Class<C> component, Class<T> operation)
	{
		return (OperationBuilder<T>)builders.get(new Key(component, operation));
	}

	public static void post(ComponentNode target, String cmd, Map<String, Object> data)
	{
		for(OperationObserver o : observers)
		{
			try
			{
				o.doPost(target, cmd, data);
			}
			catch(Exception e)
			{
				logger.log(Level.WARNING, "", e);
			}
		}
	}

	public static void addObserver(OperationObserver observer)
	{
		if(observer != null)
			observers.add(observer);
	}

	public List<OperationObserver> getObservers()
	{
		return new ArrayList<OperationObserver>(observers);
	}

	public static void removeObserver(OperationObserver observer)
	{
		if(observer != null)
			observers.remove(observer);
	}

	private static class Key
	{
		public Class<?> c;
		public Class<?> t;

		public <T extends Operation, C extends Component> Key(Class<?> c, Class<?> t)
		{
			this.c = c;
			this.t = t;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((t == null) ? 0 : t.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
				return true;
			else if(obj instanceof Key)
			{
				Key o = (Key)obj;
				return o.t == this.t && o.c == this.c;
			}
			return false;
		}
	}
}
