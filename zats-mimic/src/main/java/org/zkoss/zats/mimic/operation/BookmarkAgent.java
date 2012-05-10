/* BookmarkAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;


/**
 * To change the bookmark of a Desktop.
 * @author dennis
 */
public interface BookmarkAgent extends OperationAgent {
	
	/**
	 * change bookmark of a desktop.
	 * @param value
	 */
	void bookmark(String value);
}
