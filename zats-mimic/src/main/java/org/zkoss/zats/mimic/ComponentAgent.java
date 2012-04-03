/* ComponentAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;

import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.ui.Component;

/**
 * The component agent
 * 
 * @author pao
 */
public interface ComponentAgent extends Agent{
	
	/**
	 * get ID. of the component.
	 * 
	 * @return ID or null if it hasn't.
	 */
	String getId();
	
	/**
	 * get UUID. of the component.
	 * 
	 * @return UUID.
	 */
	String getUuid();
	
	/**
	 * get attribute by specify name.
	 * 
	 * @param name
	 *            attribute name.
	 * @return attribute value or null if not found or otherwise.
	 */
	Object getAttribute(String name);

	/**
	 * get children agents.
	 * 
	 * @return always return a list of children (may be empty).
	 */
	List<ComponentAgent> getChildren();

	/**
	 * get child by specify index.
	 * 
	 * @param index
	 * @return return child agent or null if index is out of boundary.
	 */
	ComponentAgent getChild(int index);

	/**
	 * get parent agent.
	 * 
	 * @return parent agent or null if this is root agent.
	 */
	ComponentAgent getParent();

	/**
	 * get desktop agent this component belonged to.
	 * 
	 * @return desktop agent.
	 */
	DesktopAgent getDesktop();

	/**
	 * get page agent this component belonged to.
	 * 
	 * @return page agent.
	 */
	PageAgent getPage();
	
	/**
	 * get the wrapped component
	 */
	Component getComponent();

	/**
	 * try to transfer the component agent to the target class, the target class
	 * is usually a {@link OperationAgent} or a native {@link Component} <br/>
	 * 
	 * if it cannot transfer to target class, it will throw
	 * {@link ConversationException}.
	 * 
	 * @param clazz
	 *            class of specify operation.
	 * @return operation object.
	 */
	<T> T as(Class<T> clazz);

	/**
	 * check the component agent can transfer to the target class or not
	 * 
	 * @param clazz
	 *            the class cast to.
	 * @return true if the component can cast to another class
	 */
	<T> boolean is(Class<T> clazz);
	
	/**
	 * find components matched specify selector base on this component agent.
	 * @param selector
	 */
	ComponentAgent query(String selector);
	
	/**
	 * find components matched specify selector base on this component agent.
	 * @param selector
	 */
	List<ComponentAgent> queryAll(String selector);
	
	
	/**
	 * Click on this component, A short cut of {@link ClickAgent#click()} <p/>
	 * If this component doesn't has {@link ClickAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void click();
	
	/**
	 * type on this component, it is a short cut of {@link TypeAgent#type(String)} <p/>
	 * If this component doesn't has {@link TypeAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void type(String value);
	
	/**
	 * Focus on this component, it is a short cut of {@link FocusAgent#focus()} <p/>
	 * If this component doesn't has {@link FocusAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void focus();
	
	/**
	 * Blur on this component, it is a short cut of {@link FocusAgent#blur()} <p/>
	 * If this component doesn't has {@link FocusAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void blur();
	
	/**
	 * Check on this component, it is a short cut of {@link CheckAgent#check(boolean)}<p/>
	 * If this component doesn't has {@link CheckAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void check(boolean checked);
	
	/**
	 * stroke a key on this component, it is a short cut of {@link KeyStrokeAgent#stroke(String)}<p/>
	 * If this component doesn't has {@link KeyStrokeAgent}, it will throw exception.
	 * @see #as(Class)
	 */
	void stroke(String key);
}
