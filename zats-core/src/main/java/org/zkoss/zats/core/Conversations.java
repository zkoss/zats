package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.impl.EmulatorConversation;

public class Conversations
{
	private static ThreadLocal<Conversation> c = new ThreadLocal<Conversation>();

	/**
	 * start a conversation.
	 * if there is a existed conversation, this will close last one and start a new conversation.
	 */
	public static void start()
	{
		stop();
		c.set(new EmulatorConversation());
	}

	/**
	 * close conversation and release resources.
	 */
	public static void stop()
	{
		if(c.get() != null)
		{
			c.get().destory();
			c.remove();
		}
	}

	/**
	 * navigate to specify zul path.
	 * @param zulPath the path of zul file.
	 */
	public static void open(String zulPath)
	{
		c.get().open(zulPath);
	}

	public static DesktopNode getDesktop()
	{
		return c.get().getDesktop();
	}

	public static HttpSession getSession()
	{
		return c.get().getSession();
	}
}
