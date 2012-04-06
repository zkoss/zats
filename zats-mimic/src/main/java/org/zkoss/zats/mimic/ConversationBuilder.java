/* ConversationBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import org.zkoss.zats.mimic.impl.EmulatorConversation;
import org.zkoss.zats.mimic.impl.emulator.Emulator;

/**
 * The builder for conversation.
 * 
 * @author pao
 */
public class ConversationBuilder {
	/**
	 * create new conversation to a Emulator.
	 * 
	 * @return conversation.
	 */
	public static Conversation create(Emulator emulator) {
		return new EmulatorConversation(emulator);
	}
}
