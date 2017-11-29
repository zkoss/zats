package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.junit.*;
import org.zkoss.zats.example.testcase.util.LoginOperation;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class LoginLogoutTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() {
		DesktopAgent desktop = autoClient.connect("/login.zul");

		ComponentAgent account = desktop.query("#account");
		ComponentAgent password = desktop.query("#password");
		ComponentAgent login = desktop.query("button");
		ComponentAgent msg = desktop.query("div > label");
		
		//login failed
		account.as(InputAgent.class).type("hawk");
		password.as(InputAgent.class).type("1111");
		login.as(ClickAgent.class).click();
		assertEquals("Login Failed", msg.as(Label.class).getValue());
		
		//login success
		password.as(InputAgent.class).type("1234");
		login.as(ClickAgent.class).click();
		HttpSession session = (HttpSession)((Desktop)desktop.getDelegatee()).getSession().getNativeSession();
		assertEquals(account.as(Textbox.class).getValue(), session.getAttribute("user"));
//		ComponentNode mainWin = Conversations.query("window"); no handle redirect for now
//		assertEquals("Main",mainWin.as(Window.class).getTitle());

	}
	
	@Test
	public void testLoginOperation() {
		DesktopAgent desktop = autoClient.connect("/login.zul");
		assertEquals(false, LoginOperation.login(desktop, "hawk", "1111"));
		assertEquals(true, LoginOperation.login(desktop, "hawk", "1234"));
			
	}
}
