/* PseudoClassDef.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
	// ported from zk 6.0.0 
// original package: org.zkoss.zk.ui.select
package org.zkoss.zats.common.select.impl;



/**
 * The model of pseudo class definition
 * @since 6.0.0
 * @author simonpai
 */
public interface PseudoClassDef {
	
	/**
	 * Return true if the component qualifies this pseudo class.
	 * @param ctx 
	 * @param parameters
	 */
	public boolean accept(ComponentMatchCtx ctx, String ... parameters);
	
}
