package org.zkoss.zats.mimic;

import org.zkoss.zats.mimic.impl.EmulatorConversation;

/**
 * The builder for conversation.
 * @author pao
 */
public class ConversationBuilder
{
	/**
	 * create new conversation.
	 * @return conversation.
	 */
	public static Conversation create()
	{
		return new EmulatorConversation();
	}
}
