/* Conversation.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import javax.servlet.http.HttpSession;

/**
 * Represent a connection session to a server emulator.
 * @author Hawk
 *
 */
public interface Conversation {
	
	DesktopAgent connect(String zul);


	DesktopAgent getDesktop();

	HttpSession getSession();

	// TODO post(Event) method for posting custom event of test cases
	
}
