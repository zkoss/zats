package org.zkoss.zats.mimic.node;

import java.util.List;
import org.zkoss.zk.ui.Page;

public interface PageNode extends Node
{
	/**
	 * get UUID. of this node.
	 * @return UUID.
	 */
	String getUuid();

	List<ComponentNode> getChildren();

	DesktopNode getDesktop();

	/**
	 * get the native Page.
	 * @return page
	 */
	Page cast();
}
