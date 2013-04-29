/* DownloadComposer.java

	Purpose:
		
	Description:
		
	History:
		2013/4/24 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.essentials;

import java.io.IOException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Filedownload;

/**
 * @author Hawk
 *
 */
public class DownloadComposer extends SelectorComposer<Component>{

	@Listen("onClick=#btn")
    public void download() throws IOException {
        Filedownload.save("/essentials/hello.txt", "application/octet-stream");
    }
}
