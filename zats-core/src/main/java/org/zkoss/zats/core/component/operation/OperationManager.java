package org.zkoss.zats.core.component.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zats.core.component.operation.impl.GenericClickableHandler;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

public class OperationManager
{
	private static Logger logger;
	private static List<OperationObserver> observers;
	private static Map<Key, OperationBuilder<?>> handlers;

	static
	{
		logger = Logger.getLogger(OperationObserver.class.getName());
		observers = new CopyOnWriteArrayList<OperationObserver>();
		handlers = new HashMap<OperationManager.Key, OperationBuilder<?>>();
		// TODO load default implement
		setMapping(Button.class, Clickable.class, new GenericClickableHandler());
		setMapping(Label.class, Clickable.class, new GenericClickableHandler());
		// TODO searching parent builder
		setMapping(AbstractComponent.class, Clickable.class, new GenericClickableHandler());
		// TODO load custom implement from configuration
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

	public static <T extends Operation, C extends Component> void setMapping(Class<C> component, Class<T> operation, OperationBuilder<T> handler)
	{
		if(component == null || operation == null || handler == null)
			return;
		handlers.put(new Key(component, operation), handler);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Operation, C extends Component> OperationBuilder<T> getHandler(Class<C> component, Class<T> operation)
	{
		return (OperationBuilder<T>)handlers.get(new Key(component, operation));
	}

	public static void post(Component target, String cmd, Map<String, Object> param)
	{
		for(OperationObserver o : observers)
		{
			try
			{
				o.doPost(target, cmd, param);
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
}
