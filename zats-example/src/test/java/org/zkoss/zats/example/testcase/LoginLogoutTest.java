package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.example.testcase.util.LoginOperation;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

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
		Conversations.closeAll();
	}

	@Test
	public void test() {
		DesktopAgent desktop = Conversations.open().connect("/login.zul");

		ComponentAgent account = desktop.query("#account");
		ComponentAgent password = desktop.query("#password");
		ComponentAgent login = desktop.query("button");
		ComponentAgent msg = desktop.query("div > label");
		
		//login failed
		account.as(TypeAgent.class).type("hawk");
		password.as(TypeAgent.class).type("1111");
		login.as(ClickAgent.class).click();
		assertEquals("Login Failed", msg.as(Label.class).getValue());
		
		//login success
		password.as(TypeAgent.class).type("1234");
		login.as(ClickAgent.class).click();
		HttpSession session = desktop.getConversation().getSession();
		assertEquals(account.as(Textbox.class).getValue(), session.getAttribute("user"));
//		ComponentNode mainWin = Conversations.query("window"); no handle redirect for now
//		assertEquals("Main",mainWin.as(Window.class).getTitle());

	}
	
	@Test
	public void testLoginOperation() {
		DesktopAgent desktop = Conversations.open().connect("/login.zul");
		assertEquals(false, LoginOperation.login(desktop, "hawk", "1111"));
		assertEquals(true, LoginOperation.login(desktop, "hawk", "1234"));
			
	}
}
