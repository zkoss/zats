package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.node.DesktopNode;
import org.zkoss.zats.mimic.operation.Clickable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class ConversationsTest
{
	@BeforeClass
	public static void init()
	{
//		Conversations.start("."); //from project folder
		Conversations.start("./src/main/webapp"); // user can load by configuration file
	}

	@AfterClass
	public static void end()
	{
		Conversations.stop();//
	}

	@After
	public void after()
	{
		Conversations.clean();
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
		
		ComponentNode win = Searcher.find("#win");
		assertNotNull(win);
		assertNotNull(win.cast(Window.class));
		assertEquals("my window",win.cast(Window.class).getTitle());
		
		ComponentNode msg = Searcher.find(win, "#msg"); 
		assertNotNull(msg);
		assertEquals("hello", msg.cast(Label.class).getValue());
//		assertEquals("hello", ((Label)msg.nat()).getValue());
		
		for(int i = 0; i < 10; ++i)
		{
			Searcher.find(win, "#btn").as(Clickable.class).click();
			assertEquals("s" + i, session.getAttribute("msg"));
			assertEquals("d" + i, desktop.getAttribute("msg"));
			assertEquals("" + i, msg.cast(Label.class).getValue());
		}
	}
}
