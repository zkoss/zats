/* DesktopCtrl.java

	Purpose:
		
	Description:
		
	History:
		May 22, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.zats.mimic.Downloadable;

/**
 * The interface of desktop controller.
 * To provide more control of the desktop agent for developers.
 * @author pao
 */
public interface DesktopCtrl {
	
	/**
	 * setting current downloadable file.
	 * @param downloadable an object of downloadable file or null indicated that no download currently.
	 */
	void setDownloadable(Downloadable downloadable);
}
