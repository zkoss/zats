/* UploadAgent.java

	Purpose:
		
	Description:
		
	History:
		Jun 15, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

import java.io.File;
import java.io.InputStream;

/**
 * The agent for files uploading operation.
 * Supported components with <b>upload</b> property such as Button, Menuitem and Toolbarbutton.
 * DesktopAgent can be UploadAgent for specific case such as uploading by Fileupload.get().
 * @author pao
 * @since 1.1.0
 */
public interface UploadAgent extends OperationAgent {

	/**
	 * Upload a file to ZK web application.
	 * @param fileName specify name for uploading file, should not be null.
	 * @param content content of uploaded file, should not be null.
	 * @param contentType specify type of content. If null, indicate it is binary form.
	 */
	void upload(String fileName, InputStream content, String contentType);

	/**
	 * Upload a file to ZK web application.
	 * @param file file for uploading, should not be null.
	 * @param contentType specify type of content. If null, indicate it is binary form.
	 */
	void upload(File file, String contentType);
}
