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
 * <b>Notice that you should use the same instance in one uploading iteration.</b>
 * @author pao
 * @since 1.1.0
 */
public interface UploadAgent extends OperationAgent {

	/**
	 * Upload a file from input stream to ZK web application.
	 * The input stream won't be closed, it should be closed manually.
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

	/**
	 * Invoke this method when uploading finished.
	 */
	void finish();
}
