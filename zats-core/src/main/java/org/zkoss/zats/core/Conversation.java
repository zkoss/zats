package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;

public interface Conversation
{
	void open(String zulPath);

	void destory();

	DesktopNode getDesktop();

	HttpSession getSession();
}
