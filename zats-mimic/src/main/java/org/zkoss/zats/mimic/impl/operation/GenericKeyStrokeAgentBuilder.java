/* GenericKeyStrokeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.AuUtility;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;

/**
 * @author dennis
 *
 */
public class GenericKeyStrokeAgentBuilder implements OperationAgentBuilder<KeyStrokeAgent> {
	
	private static final int ENTER = 13;
	private static final int ESC = 27;
	
	public KeyStrokeAgent getOperation(final ComponentAgent target) {
		return new KeyStrokeAgentImpl(target);
	}
	
	class KeyStrokeAgentImpl extends AgentDelegator implements KeyStrokeAgent{
		public KeyStrokeAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void stroke(String key) {
			key = key.toLowerCase().trim();
			if(key.startsWith("#")){
				if("#enter".equals(key)){
					doOnOK();
					return;
				}else if("#esc".equals(key)){
					doOnCancel();
					return;
				}
			}
			doOnCtrlKey(key);
		}

		private void doOnCtrlKey(final String key) {
			ComponentAgent et = AuUtility.lookupEventTarget(target,Events.ON_CTRL_KEY);
			if(et==null) return;
			
			//code refer to Widget.js#setCtrlKeys()
			int keyCode=-1,which = 0;
			boolean ctrlKey=false, shiftKey=false, altKey=false;
			int len = key.length();
			
			for (int j = 0; j < len; ++j) {
				if(keyCode>=0){
					throw new AgentException("allow one key code only: "+key);
				}
				char cc = key.charAt(j); //ext
				switch (cc) {
				case '^':
				case '$':
				case '@':
					if (which!=0)
						throw new AgentException("Combination of Shift, Alt and Ctrl not supported: "+key);
					which = cc == '^' ? 1: cc == '@' ? 2: 3;
					switch(which){
					case 1:
						ctrlKey = true;
						break;
					case 2:
						altKey = true;
						break;
					case 3:
						shiftKey = true;
						break;
					}
					break;
				case '#':
					int k = j + 1;
					for (; k < len; ++k) {
						char c2 = key.charAt(k);
						if ((c2 > 'z' || c2 < 'a') && (c2 > '9' || c2 < '0'))
							break;
					}
					if (k == j + 1)
						throw new AgentException("Unexpected character "+cc+" in "+key);

					String s = key.substring(j+1, k);
					if ("pgup".equals(s)) keyCode = 33;
					else if ("pgdn".equals(s)) keyCode = 34;
					else if ("end".equals(s)) keyCode = 35;
					else if ("home".equals(s)) keyCode = 36;
					else if ("left".equals(s)) keyCode = 37;
					else if ("up".equals(s)) keyCode = 38;
					else if ("right".equals(s)) keyCode = 39;
					else if ("down".equals(s)) keyCode = 40;
					else if ("ins".equals(s)) keyCode = 45;
					else if ("del".equals(s)) keyCode = 46;
					else if ("bak".equals(s)) keyCode = 8;
					else if (s.length() > 1 && s.charAt(0) == 'f') {
						int v = Integer.parseInt(s.substring(1));
						if (v == 0 || v > 12)
							throw new AgentException("Unsupported function key: #f" + v);
						keyCode = (char)(112 + v - 1);
					} else
						throw new AgentException("Unknown #"+s+" in "+key);
					which = 0;
					j = k - 1;
					break;
				default:
					if (which==0 || ((cc > 'z' || cc < 'a') && (cc > '9' || cc < '0')))
						throw new AgentException("Unexpected character "+cc+" in "+key);
					if (which == 3)
						throw new AgentException("$a - $z not supported (found in "+key+"). Allowed: $#f1, $#home and so on.");
					keyCode = Character.toUpperCase(cc);
					which = 0;
					break;
				}
			}
			if(keyCode<0){
				throw new AgentException("Code code not found: "+key);
			}
			
			String cmd = Events.ON_CTRL_KEY;
			Map<String, Object> data = EventDataManager.build(new KeyEvent(cmd, et.getComponent(), keyCode, ctrlKey,
					shiftKey, altKey, target.getComponent()));
			((ConversationCtrl)et.getConversation()).postUpdate(et.getUuid(), cmd, data);
		}

		private void doOnCancel() {
			ComponentAgent et = AuUtility.lookupEventTarget(target,Events.ON_CANCEL);
			if(et==null) return;
			
			String cmd = Events.ON_CANCEL;
			Map<String, Object> data = EventDataManager.build(new KeyEvent(cmd, et.getComponent(), ESC, false, false,
					false, target.getComponent()));
			((ConversationCtrl)et.getConversation()).postUpdate(et.getUuid(), cmd, data);
		}

		private void doOnOK() {
			ComponentAgent et = AuUtility.lookupEventTarget(target,Events.ON_OK);
			if(et==null) return;
			
			String cmd = Events.ON_OK;
			Map<String, Object> data = EventDataManager.build(new KeyEvent(cmd, et.getComponent(), ENTER, false, false,
					false, target.getComponent()));
			((ConversationCtrl)et.getConversation()).postUpdate(et.getUuid(), cmd, data);
		}
	}
}
