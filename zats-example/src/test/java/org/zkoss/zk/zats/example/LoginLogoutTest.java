package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.zats.example.util.LoginOperation;
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
		Conversations.clean();
	}

	@Test
	public void test() {
		Conversations.open("/login.zul");

		ComponentAgent account = Searcher.find("#account");
		ComponentAgent password = Searcher.find("#password");
		ComponentAgent login = Searcher.find("button");
		ComponentAgent msg = Searcher.find("div > label");
		
		//login failed
		account.as(TypeAgent.class).type("hawk");
		password.as(TypeAgent.class).type("1111");
		login.as(ClickAgent.class).click();
		assertEquals("Login Failed", msg.as(Label.class).getValue());
		
		//login success
		password.as(TypeAgent.class).type("1234");
		login.as(ClickAgent.class).click();
		HttpSession session = Conversations.getSession();
		assertEquals(account.as(Textbox.class).getValue(), session.getAttribute("user"));
//		ComponentNode mainWin = Searcher.find("window"); no handle redirect for now
//		assertEquals("Main",mainWin.as(Window.class).getTitle());

	}
	
	@Test
	public void testLoginOperation() {
		Conversations.open("/login.zul");
		assertEquals(false, LoginOperation.login("hawk","1111"));
		assertEquals(true, LoginOperation.login("hawk","1234"));
			
	}
}
