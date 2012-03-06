package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.impl.EmulatorConversation;

public class Conversations
{
	private static ThreadLocal<Conversation> local = new ThreadLocal<Conversation>();

	/**
	 * start a conversation and navigate to specify zul path.
	 * if there is a existed conversation, this will close last one and start a new conversation.
	 * @param zulPath the path of zul file.
	 */
	public static void open(String zulPath)
	{
		close();
		Conversation conversation = new EmulatorConversation();
		conversation.open(zulPath);
		local.set(conversation);
	}

	/**
	 * close conversation and release resources.
	 */
	public static void close()
	{
		if(local.get() != null)
		{
			local.get().close();
			local.remove();
		}
	}

	/**
	 * get the desktop from specify zul.
	 * @return desktop.
	 */
	public static DesktopNode getDesktop()
	{
		if(local.get() == null)
			throw new ConversationException("conversation is close");
		return local.get().getDesktop();
	}

	/**
	 * get the session.
	 * @return session or null if it doesn't have.
	 */
	public static HttpSession getSession()
	{
		if(local.get() == null)
			throw new ConversationException("conversation is close");
		return local.get().getSession();
	}
}
