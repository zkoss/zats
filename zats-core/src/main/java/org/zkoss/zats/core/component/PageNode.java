package org.zkoss.zats.core.component;

import java.util.List;

public interface PageNode extends Node
{
	List<ComponentNode> getChildren();

	DesktopNode getDesktop();
}
