package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import jakarta.servlet.http.HttpSession;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class EnvironmentTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() {
		DesktopAgent desktop = autoClient.connect("/session.zul");

		assertNotNull(desktop);
		assertNotNull(((Desktop) desktop.getDelegatee()).getSession());

		HttpSession session = (HttpSession) ((Desktop) desktop.getDelegatee()).getSession().getNativeSession();
		assertEquals("session", session.getAttribute("msg"));
		assertEquals("desktop", desktop.getAttribute("msg"));

		ComponentAgent win = desktop.query("#win");
		assertNotNull(win);
		assertNotNull(win.as(Window.class));
		assertEquals("my window", win.as(Window.class).getTitle());

		ComponentAgent msg = win.query("#msg");
		assertNotNull(msg);
		assertEquals("hello", msg.as(Label.class).getValue());
		//		assertEquals("hello", ((Label)msg.nat()).getValue());

		for (int i = 0; i < 10; ++i) {
			win.query("#btn").as(ClickAgent.class).click();
			assertEquals("s" + i, session.getAttribute("msg"));
			assertEquals("d" + i, desktop.getAttribute("msg"));
			assertEquals("" + i, msg.as(Label.class).getValue());
		}
	}
}
