/* ZatsContext.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;


/**
 * the zats context object, to init/destroy a zats runtime for the testing
 *   
 * @author Hawk
 * @author Dennis
 */
public interface ZatsContext {
	
	/**
	 * initial this context
	 * @param resourceRoot the resource root folder of the zul, it is usually the web content folder.
	 */
	public void init(String resourceRoot);

	/**
	 * destroy this context to release the resource.
	 */
	public void destroy();

	/**
	 * new a client
	 */
	public Client newClient();

	/**
	 * to close all the unclosed client
	 */
	public void cleanup();

}
