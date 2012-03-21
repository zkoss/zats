/* DesktopNode.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.node;

import java.util.List;
import org.zkoss.zk.ui.Desktop;

public interface DesktopAgent extends Agent {
	List<PageAgent> getPages();

	/**
	 * get the native Desktop.
	 * 
	 * @return desktop
	 */
	Desktop cast();
}
