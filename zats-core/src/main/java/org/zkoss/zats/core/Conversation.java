package org.zkoss.zats.core;

import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.Desktop;

public interface Conversation
{
	void open();

	void destory();

	Desktop getDesktop();

	HttpSession getSession();
}
