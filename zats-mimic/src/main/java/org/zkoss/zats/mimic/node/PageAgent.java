/* PageNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.node;

import java.util.List;
import org.zkoss.zk.ui.Page;

public interface PageAgent extends Agent {
	/**
	 * get UUID. of this node.
	 * 
	 * @return UUID.
	 */
	String getUuid();

	List<ComponentAgent> getChildren();

	DesktopAgent getDesktop();

	/**
	 * get the native Page.
	 * 
	 * @return page
	 */
	Page cast();
}
