/* OperationAgentManager.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.Version;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.RendererAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zhtml.Input;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.impl.InputElement;

public class OperationAgentManager {
	private static Map<Key, OperationAgentBuilder<? extends OperationAgent>> builders;
	private static BigInteger current; // current zk version

	static {
		builders = new HashMap<OperationAgentManager.Key, OperationAgentBuilder<? extends OperationAgent>>();

		// load current zk version
		try {
			current = Util.parseVersion(Version.class.getField("UID").get(null)
					.toString());
		} catch (Throwable e) {
			throw new RuntimeException("cannot load zk", e);
		}

		// TODO load default implement
		registerBuilder(AbstractComponent.class, ClickAgent.class,
				new GenericClickAgentBuilder());
		registerBuilder(AbstractComponent.class, FocusAgent.class,
				new GenericFocusAgentBuilder());

		registerBuilder(InputElement.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.TEXT));
		registerBuilder(Intbox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.INTEGER));
		registerBuilder(Longbox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.INTEGER));
		registerBuilder(Spinner.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.INTEGER));
		registerBuilder(Decimalbox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.DECIMAL));
		registerBuilder(Doublebox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.DECIMAL));
		registerBuilder(Doublespinner.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.DECIMAL));
		registerBuilder(Datebox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.DATE));
		registerBuilder(Timebox.class, TypeAgent.class,
				new GenericTypeAgentBuilder(GenericTypeAgentBuilder.TIME));

		registerBuilder(Listbox.class, SelectAgent.class,
				new ListboxSelectAgentBuilder());
		registerBuilder(Listbox.class, MultipleSelectAgent.class,
				new ListboxMultipleSelectAgentBuilder());
		
		registerBuilder(Listitem.class, RendererAgent.class, new ListitemRendererAgentBuilder());

		registerBuilder(Input.class, CheckAgent.class,
				new GenericCheckAgentBuilder());
		registerBuilder(Checkbox.class, CheckAgent.class,
				new GenericCheckAgentBuilder()); // include Radio.class
		registerBuilder(Menuitem.class, CheckAgent.class,
				new GenericCheckAgentBuilder());
		registerBuilder(Toolbarbutton.class, CheckAgent.class,
				new GenericCheckAgentBuilder());

		// TODO load custom implement from configuration

		// TODO
		// registerBuilder(*, Label.class, Clickable.class, new
		// GenericClickableBuilder());
		// registerBuilder(6, Label.class, Clickable.class, new
		// Generic6ClickableBuilder());

		// TODO Enhancement.
		// registerBuilder(*,*, Label.class, Clickable.class, new
		// GenericClickableBuilder());
		// registerBuilder(*,"6.0.1", Label.class, Clickable.class, new
		// GenericClickableBuilder());
		// registerBuilder("5.0.2","*", Label.class, Clickable.class, new
		// GenericClickableBuilder());
		// registerBuilder("5.0.2","6.0.1", Label.class, Clickable.class, new
		// GenericClickableBuilder());
	}

	/**
	 * register a operation builder mapping to component and operation. We can
	 * specify zk version worked on. The version text could be normal version
	 * format (e.g 6.0.0 or 5.0.7.1) or "*" sign means no specify. If specify
	 * version range doesn't include current zk version at runtime, this
	 * register will be ignored.
	 * 
	 * @param startVersion
	 *            start version (include)
	 * @param endVersion
	 *            end version (include)
	 * @param component
	 *            the component class that builder maps to ( *notice: it should
	 *            not specify interface)
	 * @param operation
	 *            the operation class that builder maps to
	 * @param builder
	 *            operation builder
	 */
	public static <T extends OperationAgent, C extends Component> void registerBuilder(
			String startVersion, String endVersion, Class<C> component,
			Class<T> operation, OperationAgentBuilder<T> builder) {
		if (startVersion == null || endVersion == null || component == null
				|| operation == null || builder == null)
			throw new IllegalArgumentException();

		// check version
		// If current isn't between start and end version, ignore this register.
		BigInteger start = "*".equals(startVersion.trim()) ? BigInteger.ZERO
				: Util.parseVersion(startVersion);
		BigInteger end = "*".equals(endVersion.trim()) ? BigInteger
				.valueOf(Long.MAX_VALUE) : Util.parseVersion(endVersion);
		if (start == null || end == null)
			throw new IllegalArgumentException("wrong version format");
		if (current.compareTo(start) < 0 || current.compareTo(end) > 0)
			return;

		// component and operation classes mapping to builder
		// builder would be replace by later register
		builders.put(new Key(component, operation), builder);
	}

	/**
	 * register a operation builder mapping to component and operation at
	 * specify zk version. This method will directly invoke
	 * registerBuilder(version, version, component, operation, builder).
	 */
	public static <T extends OperationAgent, C extends Component> void registerBuilder(
			String version, Class<C> component, Class<T> operation,
			OperationAgentBuilder<T> builder) {
		registerBuilder(version, version, component, operation, builder);
	}

	/**
	 * register a operation builder mapping to component and operation at any zk
	 * version. This method will directly invoke registerBuilder("*", "*",
	 * component, operation, builder).
	 */
	public static <T extends OperationAgent, C extends Component> void registerBuilder(
			Class<C> component, Class<T> operation, OperationAgentBuilder<T> builder) {
		registerBuilder("*", "*", component, operation, builder);
	}

	@SuppressWarnings("unchecked")
	public static <T extends OperationAgent> OperationAgentBuilder<T> getBuilder(
			Component component, Class<T> operation) {
		// search from self class to parent class
		Class<?> c = component.getClass();
		while (c != null) {
			OperationAgentBuilder<? extends OperationAgent> builder = builders
					.get(new Key(c, operation));
			if (builder != null)
				return (OperationAgentBuilder<T>) builder;
			c = c.getSuperclass();
		}
		return null; // not found
	}

	public static BigInteger getZKCurrentVersion() {
		return current;
	}

	/**
	 * for operation builder mapping
	 * 
	 * @author pao
	 */
	private static class Key {
		public Class<?> c;
		public Class<?> t;

		public <T extends OperationAgent, C extends Component> Key(Class<?> c,
				Class<?> t) {
			this.c = c;
			this.t = t;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((t == null) ? 0 : t.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			else if (obj instanceof Key) {
				Key o = (Key) obj;
				return o.t == this.t && o.c == this.c;
			}
			return false;
		}
	}
}
