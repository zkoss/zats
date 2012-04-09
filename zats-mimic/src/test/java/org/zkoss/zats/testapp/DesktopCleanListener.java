/* DesktopCleanListener.java

	Purpose:
		
	Description:
		
	History:
		2012/4/6 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testapp;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;

/**
 * @author Hawk
 *
 */
public class DesktopCleanListener implements DesktopInit, DesktopCleanup{

	public void cleanup(Desktop desktop){
		desktop.setAttribute("clean", "clean");
		System.out.println("clean desktop:"+desktop.getId());
	}
	
	public void init(Desktop desktop, Object obj){
		System.out.println("desktop:"+desktop.getId()+" init...");
	}
	
	
}
