package org.zkoss.zats.core.component.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.PageNode;
import org.zkoss.zats.core.component.operation.Operation;
import org.zkoss.zats.core.component.operation.OperationHandler;
import org.zkoss.zats.core.component.operation.OperationManager;
import org.zkoss.zk.ui.Component;

/**
 * The default implement of component node.
 * This performs operations through {@link OperationManager}.
 * @author pao
 */
public class DefaultComponentNode implements ComponentNode
{

	private PageNode pageNode;
	private Component comp;

	public DefaultComponentNode(PageNode pageNode, Component component)
	{
		this.pageNode = pageNode;
		this.comp = component;
	}

	public String getId()
	{
		return comp.getId();
	}

	public String getType()
	{
		return comp.getDefinition().getName();
	}

	public Object getAttribute(String name)
	{
		return comp.getAttribute(name);
	}

	public Map<String, Object> getAttributes()
	{
		return comp.getAttributes();
	}

	public String getUuid()
	{
		return comp.getUuid();
	}

	public List<ComponentNode> getChildren()
	{
		List<Component> children = comp.getChildren();
		List<ComponentNode> nodes = new ArrayList<ComponentNode>(children.size());
		for(Component child : children)
			nodes.add(new DefaultComponentNode(pageNode, child));
		return nodes;
	}

	public ComponentNode getChild(int index)
	{
		Component child = comp.getChildren().get(index);
		return child != null ? new DefaultComponentNode(pageNode, child) : null;
	}

	public ComponentNode getParent()
	{
		Component parent = comp.getParent();
		return parent != null ? new DefaultComponentNode(pageNode, parent) : null;
	}

	public DesktopNode getDesktop()
	{
		return pageNode.getDesktop();
	}

	public PageNode getPage()
	{
		return pageNode;
	}

	public <T extends Operation> T as(Class<T> c)
	{
		OperationHandler<T> handler = OperationManager.getHandler(comp.getClass(), c);
		return handler.getOperation(comp);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T cast(Class<T> c)
	{
		return (T)comp;
	}

}
