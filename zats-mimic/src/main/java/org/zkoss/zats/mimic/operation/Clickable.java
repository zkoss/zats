/* Clickable.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

public interface Clickable extends Operation {
	Clickable click();

	Clickable doubleClick();

	Clickable rightClick();
}
