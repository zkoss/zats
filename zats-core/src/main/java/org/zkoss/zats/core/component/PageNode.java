package org.zkoss.zats.core.component;

import java.util.List;

public interface PageNode extends Node
{
	/**
	 * get UUID. of this node.
	 * @return UUID.
	 */
	String getUuid();

	List<ComponentNode> getChildren();

	DesktopNode getDesktop();
}
