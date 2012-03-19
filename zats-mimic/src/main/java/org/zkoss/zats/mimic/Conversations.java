package org.zkoss.zats.mimic;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.mimic.node.DesktopNode;

public class Conversations
{
	private static ThreadLocal<Conversation> local = new ThreadLocal<Conversation>();

	public static void start(String resourceRoot)
	{
		stop();
		try
		{
			local.set(ConversationBuilder.create());
			local.get().start(resourceRoot);
		}
		catch(Throwable e)
		{
			local.remove();
			throw new ConversationException("", e);
		}
	}

	/**
	 * close conversation and release resources.
	 */
	public static void stop()
	{
		if(local.get() == null)
			return;
		local.get().stop();
		local.remove();
	}

	/**
	 * start a conversation and navigate to specify zul path.
	 * if there is a existed conversation, this will close last one and start a new conversation.
	 * @param zulPath the path of zul file.
	 */
	public static void open(String zulPath)
	{
		if(local.get() == null)
			throw new ConversationException("conversation is close");
		local.get().open(zulPath);
	}

	public static void clean()
	{
		if(local.get() == null)
			return;
		local.get().clean();
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
