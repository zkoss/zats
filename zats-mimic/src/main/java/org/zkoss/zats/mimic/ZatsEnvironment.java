/* ZatsEnvironment.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;


/**
 * the ZATS environment, to init/destroy a ZATS runtime for testing.
 *   
 * @author Hawk
 * @author Dennis
 */
public interface ZatsEnvironment {
	
	/**
	 * initialize testing environment
	 * @param resourceRoot the resource root folder of the zul, it is usually the web content folder.
	 */
	public void init(String resourceRoot);

	/**
	 * destroy this environment to release the resource.
	 */
	public void destroy();

	/**
	 * create a client
	 */
	public Client newClient();

	/**
	 * to close all the unclosed client
	 */
	public void cleanup();

}
