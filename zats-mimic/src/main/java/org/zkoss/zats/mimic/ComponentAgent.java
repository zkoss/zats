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
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;

/**
 * The component agent, wraps a server-side zk component.
 * 
 * @author pao
 * @author henrichen
 */
public interface ComponentAgent extends QueryAgent{
	
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
	 * Returns the first child, if any.
	 * 
	 * @since 1.2.1
	 * @return return the first child agent or null.
	 */
	ComponentAgent getFirstChild();
	
	/**
	 * Returns the last child, if any.
	 * 
	 * @since 1.2.1
	 * @return return the last child agent or null.
	 */
	ComponentAgent getLastChild();
	
	/**
	 * Returns the next sibling, if any. 
	 * 
	 * @since 1.2.1
	 * @return return the next sibling agent or null.
	 */
	ComponentAgent getNextSibling();
	
	/**
	 * Returns the previous sibling, if any. 
	 * 
	 * @since 1.2.1
	 * @return return the previous sibling agent or null.
	 */
	ComponentAgent getPreviousSibling();

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
	 * Click on this component, A short cut of {@link ClickAgent#click()} <p/>
	 * If this component doesn't support {@link ClickAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see ClickAgent
	 */
	void click();
	
	/**
	 * Type on this component, it is a short cut of {@link InputAgent#type(String)} <p/>
	 * If this component doesn't support {@link InputAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see InputAgent
	 */
	void type(String value);
	
	/**
	 * Input to this component, it is a short cut of {@link InputAgent#input(Object)} <p/>
	 * If this component doesn't support {@link InputAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see InputAgent
	 */
	void input(Object value);
	
	/**
	 * Focus this component, it is a short cut of {@link FocusAgent#focus()} <p/>
	 * If this component doesn't support {@link FocusAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see FocusAgent
	 */
	void focus();
	
	/**
	 * Blur this component, it is a short cut of {@link FocusAgent#blur()} <p/>
	 * If this component doesn't support {@link FocusAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see FocusAgent
	 */
	void blur();
	
	/**
	 * Check this component, it is a short cut of {@link CheckAgent#check(boolean)}<p/>
	 * If this component doesn't support {@link CheckAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see CheckAgent
	 */
	void check(boolean checked);
	
	/**
	 * Stroke a key on this component, it is a short cut of {@link KeyStrokeAgent#stroke(String)}<p/>
	 * If this component doesn't support {@link KeyStrokeAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see KeyStrokeAgent
	 */
	void stroke(String key);
	
	/**
	 * Select this component, it is a short cut of {@link SelectAgent#select()}<p/>
	 * If this component doesn't support {@link SelectAgent}, it will throw exception.
	 * @see #as(Class)
	 * @see SelectAgent
	 */
	void select();
}
