/* Downloadable.java

	Purpose:
		
	Description:
		
	History:
		May 25, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic;

import java.io.InputStream;

/**
 * The downloadable file interface.
 * It provides some information about the downloadable file and the input stream of the file.
 * @author pao
 */
public interface Downloadable {

	/**
	 * The file name.
	 * @return name string. 
	 */
	String getFileName();

	/**
	 * open and return a input stream contained the file content.
	 * @return the input stream or null if occurred exceptions. 
	 */
	InputStream getInputStream();
}
