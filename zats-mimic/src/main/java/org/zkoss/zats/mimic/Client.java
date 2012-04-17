/* Client.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;


/**
 * Represent a client that can connect to zul files
 * @author Hawk
 * @author Dennis
 *
 */
public interface Client {
	
	/**
	 * connect to a zul file, you have to provide the path that relative to the resource root folder
	 * 
	 * @see ZatsEnvironment#init(String)
	 */
	DesktopAgent connect(String zulPath);

	
	/**
	 * destroy this client, it will also destory all un-destroyed desktops that is created by this client
	 */
	void destroy();
	
	//TODO
	//getCookie
	//setCookie
	
}
