package org.zkoss.zats.core.component;

import java.util.List;
import org.zkoss.zats.core.ConversationException;
import org.zkoss.zats.core.component.operation.Operation;

/**
 * The basic component.
 * @author pao
 */
public interface ComponentNode extends Node
{
	/**
	 * get children nodes.
	 * @return always return a list of children (may be empty).
	 */
	List<ComponentNode> getChildren();

	/**
	 * get child by specify index.
	 * @param index
	 * @return return child node or null if index is out of boundary.
	 */
	ComponentNode getChild(int index);

	/**
	 * get parent node.
	 * @return parent node or null if this is root node.
	 */
	ComponentNode getParent();

	/**
	 * get desktop node this component belonged to.
	 * @return desktop node.
	 */
	DesktopNode getDesktop();

	/**
	 * get page node this component belonged to.
	 * @return page node.
	 */
	PageNode getPage();

	/**
	 * get specify operation for performing.
	 * if this component doesn't support the operation, it will throw {@link ConversationException}.
	 * @param c class of specify operation.
	 * @return operation object.
	 */
	<T extends Operation> T as(Class<T> c);
}
