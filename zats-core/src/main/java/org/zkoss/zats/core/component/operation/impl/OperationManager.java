package org.zkoss.zats.core.component.operation.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.Version;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.Operation;
import org.zkoss.zats.core.component.operation.Typeable;
import org.zkoss.zats.core.impl.Util;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.impl.InputElement;

public class OperationManager
{
	private static Logger logger;
	private static List<OperationObserver> observers;
	// TODO deprecate, using hierarchy searching
	private static Map<Key, OperationBuilder<? extends Operation>> builders;
	/** current zk version */
	private static BigInteger current;

	static
	{
		logger = Logger.getLogger(OperationObserver.class.getName());
		observers = new CopyOnWriteArrayList<OperationObserver>();
		builders = new HashMap<OperationManager.Key, OperationBuilder<? extends Operation>>();

		// load current zk version
		try
		{
			current = Util.parseVersion(Version.class.getField("UID").get(null).toString());
		}
		catch(Throwable e)
		{
			throw new RuntimeException("cannot load zk", e);
		}

		// TODO load default implement
		registerBuilder(AbstractComponent.class, Clickable.class, new GenericClickableBuilder());
		registerBuilder(InputElement.class, Typeable.class, new GenericTypeableBuilder());
		// TODO load custom implement from configuration

		// TODO
		// registerBuilder(*, Label.class, Clickable.class, new GenericClickableBuilder());
		// registerBuilder(6, Label.class, Clickable.class, new Generic6ClickableBuilder());

		// TODO Enhancement.
		// registerBuilder(*,*, Label.class, Clickable.class, new GenericClickableBuilder());
		// registerBuilder(*,"6.0.1", Label.class, Clickable.class, new GenericClickableBuilder());
		// registerBuilder("5.0.2","*", Label.class, Clickable.class, new GenericClickableBuilder());
		// registerBuilder("5.0.2","6.0.1", Label.class, Clickable.class, new GenericClickableBuilder());
	}

	/**
	 * register a operation builder mapping to component and operation.
	 * We can specify zk version worked on.
	 * The version text could be normal version format (e.g 6.0.0 or 5.0.7.1) or "*" sign means no specify.
	 * If specify version range doesn't include current zk version at runtime, this register will be ignored.
	 * @param startVersion start version (include)
	 * @param endVersion end version (include)
	 * @param component the component class that builder maps to ( *notice: it should not specify interface)
	 * @param operation the operation class that builder maps to
	 * @param builder operation builder
	 */
	public static <T extends Operation, C extends Component> void registerBuilder(String startVersion, String endVersion, Class<C> component, Class<T> operation, OperationBuilder<T> builder)
	{
		if(startVersion == null || endVersion == null || component == null || operation == null || builder == null)
			throw new IllegalArgumentException();

		// check version
		// If current isn't between start and end version, ignore this register.
		BigInteger start = "*".equals(startVersion.trim()) ? BigInteger.ZERO : Util.parseVersion(startVersion);
		BigInteger end = "*".equals(endVersion.trim()) ? BigInteger.valueOf(Long.MAX_VALUE) : Util.parseVersion(endVersion);
		if(start == null || end == null)
			throw new IllegalArgumentException("wrong version format");
		if(current.compareTo(start) < 0 || current.compareTo(end) > 0)
			return;

		// component and operation classes mapping to builder
		// builder would be replace by later register
		builders.put(new Key(component, operation), builder);
	}

	/**
	 * register a operation builder mapping to component and operation at specify zk version.
	 * This method will directly invoke registerBuilder(version, version, component, operation, builder).
	 */
	public static <T extends Operation, C extends Component> void registerBuilder(String version, Class<C> component, Class<T> operation, OperationBuilder<T> builder)
	{
		registerBuilder(version, version, component, operation, builder);
	}

	/**
	 * register a operation builder mapping to component and operation at any zk version.
	 * This method will directly invoke registerBuilder("*", "*", component, operation, builder).
	 */
	public static <T extends Operation, C extends Component> void registerBuilder(Class<C> component, Class<T> operation, OperationBuilder<T> builder)
	{
		registerBuilder("*", "*", component, operation, builder);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Operation> OperationBuilder<T> getBuilder(Component component, Class<T> operation)
	{
		// search from self class to parent class
		Class<?> c = component.getClass();
		while(c != null)
		{
			OperationBuilder<? extends Operation> builder = builders.get(new Key(c, operation));
			if(builder != null)
				return (OperationBuilder<T>)builder;
			c = c.getSuperclass();
		}
		return null; // not found
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

	/**
	 * post an asynchronous update event.
	 * @param target the component node which performed this event
	 * @param command
	 * @param data
	 */
	public static void post(ComponentNode target, String command, Map<String, Object> data)
	{
		for(OperationObserver o : observers)
		{
			try
			{
				o.doPost(target, command, data);
			}
			catch(Exception e)
			{
				logger.log(Level.WARNING, "", e);
			}
		}
	}

	/**
	 * for operation builder mapping
	 * @author pao
	 */
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
