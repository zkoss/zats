package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;

public interface Conversation
{
	/**
	 * start conversation.
	 * @param resourceRoot resource root path.
	 */
	void start(String resourceRoot);

	/**
	 * stop conversation.
	 */
	void stop();

	/**
	 * open specify zul page.
	 * @param zul the path related to the resource root path
	 */
	void open(String zul);

	/**
	 * clean current Desktop and release resources.
	 */
	void clean();

	DesktopNode getDesktop();

	HttpSession getSession();
}
