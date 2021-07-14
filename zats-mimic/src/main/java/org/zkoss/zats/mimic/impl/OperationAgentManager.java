/* OperationAgentManager.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.impl.operation.AuAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.ButtonUploadAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.ColumnSortAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.DesktopBookmarkAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.DialogUploadAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericClickAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericCloseAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericDragAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericFocusAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericGroupAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericHoverAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericKeyStrokeAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericMoveAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericOpenAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GridRenderAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.ListboxRenderAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.ListheaderSortAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.MenuitemUploadAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.PagingAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.PanelSizeAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.SliderInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.TextboxOpenAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.TreecolSortAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.WindowSizeAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.*;
import org.zkoss.zats.mimic.impl.operation.select.*;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.InputElement;

/**
 * <p>
 * This class maintains a mapping registry between ZK components (in specific version range) and {@link OperationAgentBuilder}. 
 * It registers all entries in the constructor and retrieve them when {@link ValueResolver} requests. 
 * </p>
 * We design the registration mechanism in order to deal with the issue: one component might have different behaviors in different versions.
 * This mechanism has several features:
 * Later registered entry overwrite previous one with the same key.
 * When get {@link OperationAgentBuilder},  it will keeping search ZK component's class and its parent class until it finds a match or fails to match.
 * 
 * 
 * 
 * @author pao
 * @author dennis
 */
public class OperationAgentManager {
	
	private static OperationAgentManager instance;
	
	public static synchronized OperationAgentManager getInstance(){
		if(instance==null){
			instance = new OperationAgentManager(); 
		}
		return instance;
	}
	
	//hold registered builders
	private Map<Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>> registeredBuilders;
	//cache the resolved builders 
	private Map<Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>> resolvedBuilders;

	public OperationAgentManager() {
		registeredBuilders = Collections.synchronizedMap(new HashMap<OperationAgentManager.Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>>());
		resolvedBuilders = Collections.synchronizedMap(new HashMap<OperationAgentManager.Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>>());

		//most common agents
		registerBuilder("9.6.0", "*", Desktop.class, new DesktopBookmarkAgentBuilder());

		registerBuilder("9.6.0", "*", AbstractComponent.class, new GenericClickAgentBuilder());
		registerBuilder("9.6.0", "*", AbstractComponent.class, new GenericKeyStrokeAgentBuilder());
		registerBuilder("9.6.0", "*", Component.class, new AuAgentBuilder());

		// the focus
		registerBuilder("9.6.0", "*", InputElement.class, new GenericFocusAgentBuilder());
		registerBuilder("9.6.0", "*", A.class, new GenericFocusAgentBuilder());
		registerBuilder("9.6.0", "*", Button.class, new GenericFocusAgentBuilder());
		registerBuilder("9.6.0", "*", Checkbox.class, new GenericFocusAgentBuilder());
		registerBuilder("9.6.0", "*", Listbox.class, new GenericFocusAgentBuilder());
		registerBuilder("9.6.0", "*", Tree.class,new GenericFocusAgentBuilder());
		
		// the inputs
		registerBuilder("9.6.0", "*", InputElement.class, new TextInputAgentBuilder());
		registerBuilder("9.6.0", "*", Intbox.class, new IntegerInputAgentBuilder());
		registerBuilder("9.6.0", "*", Longbox.class, new IntegerStringInputAgentBuilder());
		registerBuilder("9.6.0", "*", Spinner.class, new IntegerInputAgentBuilder());

		registerBuilder("9.6.0", "*", Decimalbox.class, new DecimalStringInputAgentBuilder());
		registerBuilder("9.6.0", "*", Doublebox.class, new DecimalInputAgentBuilder());
		registerBuilder("9.6.0", "*", Doublespinner.class, new DecimalInputAgentBuilder());

		registerBuilder("9.6.0", "*", Datebox.class, new DateTypeAgentBuilderZK96());
		registerBuilder("9.6.0", "*", Timebox.class, new TimeTypeAgentBuilderZK96());

		// the check
		registerBuilder("9.6.0", "*", Menuitem.class,new GenericCheckAgentBuilder());
		// the check of check box and radio button
		registerBuilder("9.6.0", "*", Checkbox.class, new GenericCheckAgentBuilder());
		registerBuilder("9.6.0", "*", Toolbarbutton.class, new GenericCheckAgentBuilder());

		// the single select
		registerBuilder("9.6.0", "*", Comboitem.class, new ComboitemSelectAgentBuilder());
		registerBuilder("9.6.0", "*", Tab.class, new TabSelectAgentBuilder());
		registerBuilder("9.6.0", "*", Listitem.class, new LisitemSelectAgentBuilder());
		registerBuilder("9.6.0", "*", Treeitem.class, new TreeSelectAgentBuilder());

		// the multiple select
		registerBuilder("9.6.0", "*", Listitem.class, new ListitemMultipleSelectAgentBuilder());
		registerBuilder("9.6.0", "*", Treeitem.class, new TreeitemMultipleSelectAgentBuilder());

		// select by index
		registerBuilder("9.6.0", "*", Selectbox.class, new SelectboxSelectByIndexAgentBuilder());

		// the open
		registerBuilder("9.6.0", "*", Groupbox.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Detail.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Group.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Listgroup.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Treeitem.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Window.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Panel.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Center.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", North.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", East.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", West.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", South.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Splitter.class, new GenericOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Popup.class, new GenericOpenAgentBuilder());
		
		registerBuilder("9.6.0", "*", Bandbox.class, new TextboxOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Combobox.class, new TextboxOpenAgentBuilder());
		registerBuilder("9.6.0", "*", Combobutton.class, new GenericOpenAgentBuilder());

		// the close
		registerBuilder("9.6.0", "*", Window.class,new GenericCloseAgentBuilder());
		registerBuilder("9.6.0", "*", Panel.class, new GenericCloseAgentBuilder());
		registerBuilder("9.6.0", "*", Tab.class,new GenericCloseAgentBuilder());

		// the render
		registerBuilder("9.6.0", "*", Listbox.class, new ListboxRenderAgentBuilder());
		registerBuilder("9.6.0", "*", Grid.class, new GridRenderAgentBuilder());

		// the resize
		registerBuilder("9.6.0", "*", Window.class, new WindowSizeAgentBuilder());
		registerBuilder("9.6.0", "*", Panel.class, new PanelSizeAgentBuilder());
//		registerBuilder("9.6.0", "*", Column.class, new HeaderSizeAgentBuilder());
//		registerBuilder("9.6.0", "*", Listheader.class, new HeaderSizeAgentBuilder());
//		registerBuilder("9.6.0", "*", Treecol.class, new HeaderSizeAgentBuilder());

		//drag & drop
		registerBuilder("9.6.0", "*", HtmlBasedComponent.class, new GenericDragAgentBuilder());
		
		//hover
		registerBuilder("9.6.0", "*", HtmlBasedComponent.class, new GenericHoverAgentBuilder());
		
		//paging
		registerBuilder("9.6.0", "*", Paging.class, new PagingAgentBuilder());
		
		//group
		registerBuilder("9.6.0", "*", Column.class, new GenericGroupAgentBuilder());

		//sort
		registerBuilder("9.6.0", "*", Column.class, new ColumnSortAgentBuilder());
		registerBuilder("9.6.0", "*", Listheader.class, new ListheaderSortAgentBuilder());
		registerBuilder("9.6.0", "*", Treecol.class, new TreecolSortAgentBuilder());
		
		// the scroll
		registerBuilder("9.6.0", "*", Slider.class, new SliderInputAgentBuilder());
		
		// the move
		registerBuilder("9.6.0", "*", Window.class, new GenericMoveAgentBuilder());
		registerBuilder("9.6.0", "*", Panel.class, new GenericMoveAgentBuilder());
		
		// upload
		registerBuilder("9.6.0", "*", Button.class, new ButtonUploadAgentBuilder());
		registerBuilder("9.6.0", "*", Fileupload.class, new ButtonUploadAgentBuilder());
		registerBuilder("9.6.0", "*", Toolbarbutton.class, new ButtonUploadAgentBuilder());
		registerBuilder("9.6.0", "*", Menuitem.class, new MenuitemUploadAgentBuilder());
		registerBuilder("9.6.0", "*", Desktop.class, new DialogUploadAgentBuilder());
		
		//----------special case ---

		if (Util.hasClass("org.zkoss.bind.Binder")) { // zkbind
			ValueResolverManager.getInstance().registerResolver("9.6.0", "*", "bind",
					"org.zkoss.zats.mimic.impl.BindValueResolver");
		}

		String colorboxClassName = "org.zkoss.zkex.zul.Colorbox"; // zkex
		if(Util.hasClass(colorboxClassName)){
			registerBuilder("9.6.0", "*", colorboxClassName,
					"org.zkoss.zats.mimic.impl.operation.input.ColorboxInputAgentBuilder");
		}

		String navitemClassName = "org.zkoss.zkmax.zul.Navitem"; // zkmax only
		if(Util.hasClass(navitemClassName)){
			registerBuilder("9.6.0", "*", navitemClassName,
					"org.zkoss.zats.mimic.impl.operation.select.NavitemSelectAgentBuilder");
		}

		// the ckeditor (optional)
		String ckeditorClassName = "org.zkforge.ckez.CKeditor";
		if(Util.hasClass(ckeditorClassName)){
			registerBuilder("9.6.0", "*", ckeditorClassName,
					"org.zkoss.zats.mimic.impl.operation.input.TextInputAgentBuilder");
		}
	}

	/**
	 * Register a operation builder mapping to component and operation. We can
	 * specify zk version worked on. The version text could be normal version
	 * format (e.g. 6.0.0 or 5.0.7.1) or "*" sign means no specify. If specify
	 * version range doesn't include current zk version at runtime, this
	 * register will be ignored.
	 * <p/>
	 * 
	 * Use this API if the component is only in a particular zk version only to
	 * avoid initial exception.
	 * <p/>
	 * 
	 * @param startVersion
	 *            start version (include)
	 * @param endVersion
	 *            end version (include)
	 * @param delegateeClass
	 *            the component class that builder maps to ( *notice: it should
	 *            not specify interface)
	 * @param builderClazz
	 *            operation builder
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <O extends OperationAgent> void registerBuilder(String startVersion, String endVersion,
			String delegateeClass, String builderClazz) {
		if (startVersion == null || endVersion == null || delegateeClass == null || builderClazz == null)
			throw new IllegalArgumentException();

		if (!Util.checkVersion(startVersion, endVersion))
			return;

		Class clz = null;
		try {
			clz = Class.forName(delegateeClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("delegateeClass " + delegateeClass + " not found ", e);
		}
		OperationAgentBuilder<Agent,O> builder = null;
		try {
			Class buildClz = Class.forName(builderClazz);
			builder = (OperationAgentBuilder<Agent,O>) buildClz.newInstance();
		} catch (Exception x) {
			throw new IllegalArgumentException(x.getMessage(), x);
		}

		registerBuilder(startVersion, endVersion, clz, builder);
	}

	/**
	 * register a operation builder mapping to component and operation. We can
	 * specify zk version worked on. The version text could be normal version
	 * format (e.g 6.0.0 or 5.0.7.1) or "*" sign means no specify. If specify
	 * version range doesn't include current zk version at runtime, this
	 * register will be ignored.
	 * @param startVersion
	 *            start version (include)
	 * @param endVersion
	 *            end version (include)
	 * @param delegateeClass
	 *            the component class that builder maps to ( *notice: it should
	 *            not specify interface)
	 * @param builder
	 *            operation builder
	 */
	public <O extends OperationAgent, C extends Object> void registerBuilder(String startVersion,
			String endVersion, Class<C> delegateeClass, OperationAgentBuilder<? extends Agent,O> builder) {

		if (startVersion == null || endVersion == null || delegateeClass == null || builder == null)
			throw new IllegalArgumentException();

		if (!Util.checkVersion(startVersion, endVersion))
			return;

		// component and operation classes mapping to builder
		// builder would be replace by later register
		registeredBuilders.put(new Key(delegateeClass, builder.getOperationClass()), builder);
	}

	/**
	 * Return a corresponding OperationAgentBuilder for delegatee supported operation class.
	 * It will search in registry with delegatee's class first. If not found, it keeps searching with delegatee's parent class until found or failed.
	 * @param delegatee
	 * @param operation 
	 * @return the operation agent builder
	 */
	@SuppressWarnings("unchecked")
	public <O extends OperationAgent> OperationAgentBuilder<Agent, O> getBuilder(Object delegatee,
			Class<O> operation) {
		// search from self class to parent class
		Class<?> c = delegatee.getClass();
		
		Key key = new Key(c, operation);
		OperationAgentBuilder<? extends Agent, ? extends OperationAgent> builder = resolvedBuilders.get(key);
		if(builder==null){
			//cache the lookup
			builder = lookupBuilder(c, operation);
			if(builder!=null){
				resolvedBuilders.put(key, builder);
			}
		}
		
		return (OperationAgentBuilder<Agent, O>)builder;
	}
	
	@SuppressWarnings("unchecked")
	private <O extends OperationAgent> OperationAgentBuilder<Agent, O> lookupBuilder(Class<?> delegatee, Class<O> operation) {
		// search from class to super class and interfaces
		OperationAgentBuilder<? extends Agent, ? extends OperationAgent> builder;
		//look super first
		Class<?> c = delegatee;
		while (c != null) {
			builder = registeredBuilders.get(new Key(c, operation));
			if (builder != null)
				return (OperationAgentBuilder<Agent, O>) builder;
			//check super
			c = c.getSuperclass();
		}
		
		//then lookup interface
		c = delegatee;
		while (c != null) {
			Class<?>[] ifs = c.getInterfaces();
			for (Class<?> i : ifs) {
				builder = lookupBuilder(i, operation);
				if (builder != null) {
					return (OperationAgentBuilder<Agent, O>)builder;
				}
			}
			//check interface of super
			c = c.getSuperclass();
		}
		return null; // not found
	}

	/**
	 * for operation builder mapping
	 * @author pao
	 */
	private static class Key {
		public Class<?> c;
		public Class<?> o;

		public <O extends OperationAgent, C extends Object> Key(Class<C> c, Class<O> o) {
			this.c = c;
			this.o = o;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((o == null) ? 0 : o.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			else if (obj instanceof Key) {
				Key o = (Key) obj;
				return o.o == this.o && o.c == this.c;
			}
			return false;
		}
	}
}
