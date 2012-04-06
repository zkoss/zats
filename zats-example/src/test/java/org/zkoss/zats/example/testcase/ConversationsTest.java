package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
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
		Conversations.closeAll();
	}

	@Test
	public void test()
	{
		DesktopAgent desktop = Conversations.open().connect("/session.zul");

		assertNotNull(desktop.getConversation().getSession());
		assertNotNull(desktop);

		HttpSession session = desktop.getConversation().getSession();
		assertEquals("session", session.getAttribute("msg"));
		assertEquals("desktop", desktop.getAttribute("msg"));
		
		ComponentAgent win = desktop.query("#win");
		assertNotNull(win);
		assertNotNull(win.as(Window.class));
		assertEquals("my window",win.as(Window.class).getTitle());
		
		ComponentAgent msg = win.query("#msg"); 
		assertNotNull(msg);
		assertEquals("hello", msg.as(Label.class).getValue());
//		assertEquals("hello", ((Label)msg.nat()).getValue());
		
		for(int i = 0; i < 10; ++i)
		{
			win.query("#btn").as(ClickAgent.class).click();
			assertEquals("s" + i, session.getAttribute("msg"));
			assertEquals("d" + i, desktop.getAttribute("msg"));
			assertEquals("" + i, msg.as(Label.class).getValue());
		}
	}
}
