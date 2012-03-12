package org.zkoss.zats.core.component;

import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.impl.EmulatorConversation;

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
