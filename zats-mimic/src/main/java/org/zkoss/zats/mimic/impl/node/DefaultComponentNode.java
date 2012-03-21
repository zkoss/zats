/* DefaultComponentNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.impl.operation.OperationBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationManager;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.node.DesktopNode;
import org.zkoss.zats.mimic.node.PageNode;
import org.zkoss.zats.mimic.operation.Operation;
import org.zkoss.zk.ui.Component;

/**
 * The default implement of component node. This performs operations through
 * {@link OperationManager}.
 * 
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
		List<ComponentNode> nodes = new ArrayList<ComponentNode>(
				children.size());
		for (Component child : children)
			nodes.add(new DefaultComponentNode(pageNode, child));
		return nodes;
	}

	public ComponentNode getChild(int index) {
		Component child = (Component) comp.getChildren().get(index);
		return child != null ? new DefaultComponentNode(pageNode, child) : null;
	}

	public ComponentNode getParent() {
		Component parent = comp.getParent();
		return parent != null ? new DefaultComponentNode(pageNode, parent)
				: null;
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

	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> clazz) {
		if (Operation.class.isAssignableFrom(clazz)) {
			Class<Operation> opc = (Class<Operation>) clazz;
			OperationBuilder<Operation> builder = OperationManager.getBuilder(
					comp, opc);
			if (builder != null)
				return (T) builder.getOperation(this);
		} else if (clazz.isInstance(comp)) {
			return (T) comp;
		}
		throw new ConversationException(getType() + " doesn't support "
				+ clazz.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> boolean is(Class<T> clazz) {
		if (Operation.class.isAssignableFrom(clazz)) {
			Class<Operation> opc = (Class<Operation>) clazz;
			OperationBuilder<Operation> builder = OperationManager.getBuilder(
					comp, opc);
			return builder != null;
		} else if (Component.class.isAssignableFrom(clazz)) {
			return clazz.isInstance(comp);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return comp.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return comp.equals(obj);
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.node.ComponentNode#find(java.lang.String)
	 */
	public ComponentNode find(String selector) {
		return Searcher.find(this,selector);
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.node.ComponentNode#findAll(java.lang.String)
	 */
	public List<ComponentNode> findAll(String selector) {
		return Searcher.findAll(this,selector);
	}
	
	public String toString(){
		return new StringBuilder().append(getClass().getSimpleName())
			.append("@").append(Integer.toHexString(System.identityHashCode(this)))	
			.append("[").append(comp.toString()).append("]")
			.toString();
	}
	
	//for internal test only utility class
	public void dump() {
		StringBuilder sb = new StringBuilder();
		dump(sb,this,0);
		System.out.println(sb.toString());
	}
	private void dump(StringBuilder sb, ComponentNode node,int indent) {
		List<ComponentNode> children = node.getChildren();
		StringBuffer idt = new StringBuffer();
		for(int i=0;i<indent;i++){
			idt.append("  ");
		}
		sb.append(idt);
		Component zkc = node.as(Component.class);
		String nm = zkc.getClass().getSimpleName(); 
		sb.append("<");
		sb.append(nm);
		sb.append(" uuid=\"").append(zkc.getUuid()).append("\"");
		
		String id = zkc.getId();
		if(id != null ){
			sb.append(" id=\"").append(id).append("\"");
		}
		
		if(children.size()>0){
			sb.append(">\n");
		}else{
			sb.append(" />\n");
		}
		
		for(ComponentNode w:children){
			dump(sb,w,indent+1);
		}
		
		if(children.size()>0){
			sb.append(idt);
			sb.append("</").append(nm).append(">\n");
		}
	}
}
