package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.servlet.http.HttpSession;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.core.Conversations;
import org.zkoss.zats.core.Searcher;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zul.Label;

public class ConversationsTest
{
	@BeforeClass
	public static void init()
	{
		Conversations.start("./src/main/webapp"); // user can load by configuration file
	}

	@AfterClass
	public static void end()
	{
		Conversations.stop();
	}

	@Test
	public void test()
	{
		Conversations.open("/test.zul");

		assertNotNull(Conversations.getSession());
		assertNotNull(Conversations.getDesktop());

		HttpSession session = Conversations.getSession();
		DesktopNode desktop = Conversations.getDesktop();
		assertEquals("session", session.getAttribute("msg"));
		assertEquals("desktop", desktop.getAttribute("msg"));
		assertNotNull(Searcher.find(desktop, "#msg"));
		assertNotNull(Searcher.find(desktop, "#msg").cast(Label.class));
		assertEquals("hello", Searcher.find(desktop, "#msg").cast(Label.class).getValue());

		for(int i = 0; i < 10; ++i)
		{
			Searcher.find(desktop, "#btn").as(Clickable.class).click();
			assertEquals("s" + i, session.getAttribute("msg"));
			assertEquals("d" + i, desktop.getAttribute("msg"));
			assertEquals("" + i, Searcher.find(desktop, "#msg").cast(Label.class).getValue());
		}

		Conversations.clean();
	}
}
