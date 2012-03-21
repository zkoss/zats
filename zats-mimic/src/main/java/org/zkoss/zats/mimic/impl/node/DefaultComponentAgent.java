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
import org.zkoss.zats.mimic.impl.operation.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationAgentManager;
import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.node.DesktopAgent;
import org.zkoss.zats.mimic.node.PageAgent;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.Component;

/**
 * The default implement of component node. This performs operations through
 * {@link OperationAgentManager}.
 * 
 * @author pao
 */
public class DefaultComponentAgent implements ComponentAgent {

	private PageAgent pageNode;
	private Component comp;

	public DefaultComponentAgent(PageAgent pageNode, Component component) {
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

	public List<ComponentAgent> getChildren() {
		List<Component> children = comp.getChildren();
		List<ComponentAgent> nodes = new ArrayList<ComponentAgent>(
				children.size());
		for (Component child : children)
			nodes.add(new DefaultComponentAgent(pageNode, child));
		return nodes;
	}

	public ComponentAgent getChild(int index) {
		Component child = (Component) comp.getChildren().get(index);
		return child != null ? new DefaultComponentAgent(pageNode, child) : null;
	}

	public ComponentAgent getParent() {
		Component parent = comp.getParent();
		return parent != null ? new DefaultComponentAgent(pageNode, parent)
				: null;
	}

	public Conversation getConversation() {
		return getDesktop().getConversation();
	}

	public DesktopAgent getDesktop() {
		return pageNode.getDesktop();
	}

	public PageAgent getPage() {
		return pageNode;
	}

	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> clazz) {
		if (OperationAgent.class.isAssignableFrom(clazz)) {
			Class<OperationAgent> opc = (Class<OperationAgent>) clazz;
			OperationAgentBuilder<OperationAgent> builder = OperationAgentManager.getBuilder(
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
		if (OperationAgent.class.isAssignableFrom(clazz)) {
			Class<OperationAgent> opc = (Class<OperationAgent>) clazz;
			OperationAgentBuilder<OperationAgent> builder = OperationAgentManager.getBuilder(
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
	public ComponentAgent find(String selector) {
		return Searcher.find(this,selector);
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zats.mimic.node.ComponentNode#findAll(java.lang.String)
	 */
	public List<ComponentAgent> findAll(String selector) {
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
	private void dump(StringBuilder sb, ComponentAgent node,int indent) {
		List<ComponentAgent> children = node.getChildren();
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
		
		for(ComponentAgent w:children){
			dump(sb,w,indent+1);
		}
		
		if(children.size()>0){
			sb.append(idt);
			sb.append("</").append(nm).append(">\n");
		}
	}
}
