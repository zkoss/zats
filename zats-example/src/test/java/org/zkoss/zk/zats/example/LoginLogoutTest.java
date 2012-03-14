package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.core.Conversations;
import org.zkoss.zats.core.Searcher;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.Typeable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LoginLogoutTest {
	@BeforeClass
	public static void init() {
		Conversations.start("./src/main/webapp"); // user can load by
													// configuration file
	}

	@AfterClass
	public static void end() {
		Conversations.stop();
	}

	@After
	public void after() {
		Conversations.clean();
	}

	@Test
	public void test() {
		Conversations.open("/login.zul");

		ComponentNode account = Searcher.find("#account");
		ComponentNode password = Searcher.find("#password");
		ComponentNode login = Searcher.find("button");
		ComponentNode msg = Searcher.find("div > label");
		
		//login failed
		account.as(Typeable.class).type("hawk");
		password.as(Typeable.class).type("1111");
		login.as(Clickable.class).click();
		assertEquals("Login Failed", msg.cast(Label.class).getValue());
		
		//login success
		password.as(Typeable.class).type("1234");
		login.as(Clickable.class).click();
		HttpSession session = Conversations.getSession();
		assertEquals(account.cast(Textbox.class).getValue(), session.getAttribute("auth"));
		ComponentNode mainWin = Searcher.find("window");
		assertEquals("Main",mainWin.cast(Window.class).getTitle());

//		ComponentNode win = Searcher.find("#win");
//		assertNotNull(win);
//		assertNotNull(win.cast(Window.class));
//		assertEquals("my window", win.cast(Window.class).getTitle());

		// assertEquals("hello", ((Label)msg.nat()).getValue());

	}
}
