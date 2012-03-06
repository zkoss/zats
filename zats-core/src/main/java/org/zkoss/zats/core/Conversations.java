package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.impl.EmulatorConversation;

public class Conversations
{
	private static ThreadLocal<Conversation> c = new ThreadLocal<Conversation>();

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
		c.set(conversation);
	}

	/**
	 * close conversation and release resources.
	 */
	public static void close()
	{
		if(c.get() != null)
		{
			c.get().close();
			c.remove();
		}
	}

	/**
	 * get the desktop from specify zul.
	 * @return desktop.
	 */
	public static DesktopNode getDesktop()
	{
		return c.get().getDesktop();
	}

	/**
	 * get the session.
	 * @return session or null if it doesn't have.
	 */
	public static HttpSession getSession()
	{
		return c.get().getSession();
	}
}
