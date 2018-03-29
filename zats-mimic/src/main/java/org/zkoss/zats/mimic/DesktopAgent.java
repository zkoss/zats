/* DesktopAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.List;

import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;

/**
 * The desktop agent, represents a server-side ZK desktop
 *
 * @author pao
 * @author Dennis
 * @author henrichen
 */
public interface DesktopAgent extends QueryAgent {
    /**
     * get ID. of this the desktop.
     *
     * @return ID or null if it hasn't.
     */
    String getId();


    /**
     * get pages in this desktop
     */
    List<PageAgent> getPages();


    /**
     * get attribute by specify name.
     *
     * @param name attribute name.
     * @return attribute value or null if not found or otherwise.
     */
    Object getAttribute(String name);

    /**
     * destroy this desktop.
     */
    void destroy();

    /**
     * Get the current downloadable resource.
     *
     * @return downloadable resource or null if there is no such resource.
     * @since 1.1.0
     */
    Resource getDownloadable();

    /**
     * Get the attached {@link Desktop} instance.
     *
     * @return the attached {@link Desktop} instance.
     */
    Desktop getDesktop();
}
