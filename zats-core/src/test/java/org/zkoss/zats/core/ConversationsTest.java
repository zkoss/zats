package org.zkoss.zats.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.operation.Clickable;

public class ConversationsTest
{
	@Test
	public void test()
	{
		assertNull(Conversations.getSession());
		assertNull(Conversations.getDesktop());

		String zul = ConversationsTest.class.getResource("test.zul").getPath().substring(1);
		Conversations.open(zul);

		HttpSession session = Conversations.getSession();
		DesktopNode desktop = Conversations.getDesktop();
		assertEquals("session", session.getAttribute("msg"));
		assertEquals("desktop", desktop.getAttribute("msg"));
		assertEquals("hello", Searcher.findFirst(desktop, "$msg").getAttribute("value"));

		for(int i = 0; i < 10; ++i)
		{
			Searcher.findFirst(desktop, "$btn").as(Clickable.class).click();
			assertEquals("s" + i, session.getAttribute("msg"));
			assertEquals("d" + i, desktop.getAttribute("msg"));
			assertEquals("" + i, Searcher.findFirst(desktop, "$msg").getAttribute("value"));
		}

		Conversations.close();
	}
}
