/* KeyStrokeAgent.java

	Purpose:
		
	Description:
		
	History:
		2012/3/21 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * This agent fires 3 zk component events that related to key, <b>onOK</b>, <b>onCancel</b> and <b>onCtrlKey</b>. 
 * @author dennis
 *
 */
public interface KeyStrokeAgent extends OperationAgent{

	/** 
	 * Following keys triggers <b>onOK</b> and <b>onCancel</b>
	 * <p>
	 * <dl>
	 * <dt>#enter</dt>
	 * <dd>The ENTER key, it post a <b>onOK</b> event</dd>
	 * <dt>#esc</dt>
	 * <dd>The ESC key, it post a <b>onCancel</b> event</dd>
	 * </p>
	 * 
	 * <p>Following keys trigger <b>onCtrlKey</b> event:
	 * <dt>^k</dt>
	 * <dd>A control key, i.e., Ctrl+k, where k could be a~z, 0~9, #n</dd>
	 * <dt>@k</dt>
	 * <dd>A alt key, i.e., Alt+k, where k could be a~z, 0~9, #n</dd>
	 * <dt>$n</dt>
	 * <dd>A shift key, i.e., Shift+n, where n could be #n.
	 * Note: $a ~ $z are not supported.</dd>
	 * <dt>#home</dt>
	 * <dd>Home</dd>
	 * <dt>#end</dt>
	 * <dd>End</dd>
	 * <dt>#ins</dt>
	 * <dd>Insert</dd>
	 * <dt>#del</dt>
	 * <dd>Delete</dd>
	 * <dt>#bak</dt>
	 * <dd>Backspace</dd> 
	 * <dt>#left</dt>
	 * <dd>Left arrow</dd>
	 * <dt>#right</dt>
	 * <dd>Right arrow</dd>
	 * <dt>#up</dt>
	 * <dd>Up arrow</dd>
	 * <dt>#down</dt>
	 * <dd>Down arrow</dd>
	 * <dt>#pgup</dt>
	 * <dd>PageUp</dd>
	 * <dt>#pgdn</dt>
	 * <dd>PageDn</dd>
	 * <dt>#f1 #f2 ... #f12</dt>
	 * <dd>Function keys representing F1, F2, ... F12</dd>
	 * </dl>
	 *
	 * <p>For example,
	 * <dl>
	 * <dt>^#left</dt>
	 * <dd>It means Ctrl+Left.</dd>
	 * <dt>^#f1</dt>
	 * <dd>It means Ctrl+F1.</dd>
	 * <dt>@#f3</dt>
	 * <dd>It means Alt+F3.</dd>
	 * </dl>
	 *
	 * <p>Note: it doesn't support Ctrl+Alt, Shift+Ctrl, Shift+Alt or Shift+Ctrl+Alt.
	 * @param String key
	 */
	void stroke(String key);
	
}
