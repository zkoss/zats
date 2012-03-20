/* MultipleSelectable.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * to select multiple items of a component
 * @author dennis
 *
 */
public interface MultipleSelectable extends Operation{
	
	/**
	 * to select a item, it will not clean another selection.
	 * if you select a item that was already selected, the this operation do nothing.
	 * @param index the index array of items to select. 
	 */
	public MultipleSelectable select(int index);
	
	/**
	 * to deselect a item, it will not clean another selection.
	 * if you deselect a item that wasn't already selected, the this operation do nothing. 
	 * @param index
	 * @return
	 */
	public MultipleSelectable deselect(int index);
}
