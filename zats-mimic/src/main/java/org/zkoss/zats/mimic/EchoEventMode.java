/* EchoEventMode.java

	Purpose:
		
	Description:
		
	History:
		Apr 19, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic;

/**
 * The enumeration of echo event mode
 * @author pao
 * @since 1.1.0
 */
public enum EchoEventMode {

	/** immediately reply custom events back to server when receiving echo events	 */
	IMMEDIATE,

	/** reply custom events back to server when next AU event posting */
	PIGGYBACK
}
