package org.zkoss.zats.core;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.ComponentNode;
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
	
	/**
	 * post an asynchronous update event.
	 * @param target the component node which performed this event
	 * @param command command
	 * @param data data for update
	 */
	void postUpdate(ComponentNode target, String cmd, Map<String, Object> data);
}
