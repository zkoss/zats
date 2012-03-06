package org.zkoss.zats.core;

import java.io.Closeable;
import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.component.DesktopNode;

public interface Conversation extends Closeable
{
	void open(String zulPath);

	void close();

	DesktopNode getDesktop();

	HttpSession getSession();
}
