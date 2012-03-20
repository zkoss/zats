/* DefaultComponentNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.impl.operation.OperationBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationManager;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.node.DesktopNode;
import org.zkoss.zats.mimic.node.PageNode;
import org.zkoss.zats.mimic.operation.Operation;
import org.zkoss.zk.ui.Component;

/**
 * The default implement of component node.
 * This performs operations through {@link OperationManager}.
 * @author pao
 */
public class DefaultComponentNode implements ComponentNode {

	private PageNode pageNode;
	private Component comp;

	public DefaultComponentNode(PageNode pageNode, Component component) {
		this.pageNode = pageNode;
		this.comp = component;
	}

	public String getId() {
		return comp.getId();
	}

	public String getType() {
		return comp.getDefinition().getName();
	}

	public Object getAttribute(String name) {
		return comp.getAttribute(name);
	}

	public Map<String, Object> getAttributes() {
		return comp.getAttributes();
	}

	public String getUuid() {
		return comp.getUuid();
	}

	public List<ComponentNode> getChildren() {
		List<Component> children = comp.getChildren();
		List<ComponentNode> nodes = new ArrayList<ComponentNode>(children.size());
		for(Component child : children)
			nodes.add(new DefaultComponentNode(pageNode, child));
		return nodes;
	}

	public ComponentNode getChild(int index) {
		Component child = (Component)comp.getChildren().get(index);
		return child != null ? new DefaultComponentNode(pageNode, child) : null;
	}

	public ComponentNode getParent() {
		Component parent = comp.getParent();
		return parent != null ? new DefaultComponentNode(pageNode, parent) : null;
	}

	public Conversation getConversation() {
		return getDesktop().getConversation();
	}

	public DesktopNode getDesktop() {
		return pageNode.getDesktop();
	}

	public PageNode getPage() {
		return pageNode;
	}

	public <T extends Operation> T as(Class<T> operation) {
		OperationBuilder<T> builder = OperationManager.getBuilder(comp, operation);
		if(builder == null)
			throw new UnsupportedOperationException(getType() + " doesn't support " + operation.getName());
		return builder.getOperation(this);
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> c){
		return (T)comp;
	}

	public <T> boolean isCastable(Class<T> c) {
		return c.isInstance(comp);
	}

	@Override
	public int hashCode() {
		return comp.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return comp.equals(obj);
	}
}
