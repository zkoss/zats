/* Downloadable.java

	Purpose:
		
	Description:
		
	History:
		May 25, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic;

import java.io.IOException;
import java.io.InputStream;

/**
 * The Resource interface.
 * It provides some information about a resource and the input stream of it.
 * @author pao
 * @since 1.1.0
 */
public interface Resource {

	/**
	 * The resource name.
	 * @return name string. 
	 */
	String getName();

	/**
	 * Open and return a input stream for the resource. The caller have to close the input-stream after the using of it.
	 * @return the input stream 
	 * @throws IOException 
	 */
	InputStream getInputStream() throws IOException;
}
