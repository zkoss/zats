/* ComponentNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.node;

import java.util.List;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.operation.Operation;
import org.zkoss.zk.ui.Component;

/**
 * The basic component.
 * 
 * @author pao
 */
public interface ComponentNode extends Node {
	/**
	 * get UUID. of this node.
	 * 
	 * @return UUID.
	 */
	String getUuid();

	/**
	 * get children nodes.
	 * 
	 * @return always return a list of children (may be empty).
	 */
	List<ComponentNode> getChildren();

	/**
	 * get child by specify index.
	 * 
	 * @param index
	 * @return return child node or null if index is out of boundary.
	 */
	ComponentNode getChild(int index);

	/**
	 * get parent node.
	 * 
	 * @return parent node or null if this is root node.
	 */
	ComponentNode getParent();

	/**
	 * get desktop node this component belonged to.
	 * 
	 * @return desktop node.
	 */
	DesktopNode getDesktop();

	/**
	 * get page node this component belonged to.
	 * 
	 * @return page node.
	 */
	PageNode getPage();

	/**
	 * try to transfer the component node to the target class, the target class
	 * is usually a {@link Operation} or a native {@link Component} <br/>
	 * 
	 * if it cannot transfer to target class it will throw
	 * {@link ConversationException}.
	 * 
	 * @param clazz
	 *            class of specify operation.
	 * @return operation object.
	 */
	<T> T as(Class<T> clazz);

	/**
	 * check the component node can transfer to the target class or not
	 * 
	 * @param clazz
	 *            the class cast to.
	 * @return true if the component can cast to another class
	 */
	<T> boolean is(Class<T> clazz);
	
	/**
	 * find components matched specify selector base on this component node.
	 * @param selector
	 */
	ComponentNode find(String selector);
	
	/**
	 * find components matched specify selector base on this component node.
	 * @param selector
	 */
	List<ComponentNode> findAll(String selector);
}
