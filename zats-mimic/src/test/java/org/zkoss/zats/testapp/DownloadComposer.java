/* DownloadComposer.java

	Purpose:
		
	Description:
		
	History:
		Apr 26, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testapp;

import java.io.File;
import java.io.Writer;

import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;

/**
 * @author pao
 */
public class DownloadComposer extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// create a temp file for testing
		File temp = null;
		Writer writer = null;
		try {
			temp = java.io.File.createTempFile("zats-", ".tmp");
			temp.deleteOnExit();
			writer = new java.io.FileWriter(temp);
			writer.write("Hello ZK!\nThis is a test file!");
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			Util.close(writer);
		}
		
		Filedownload.save(temp, "application/octet-stream");
	}
}
