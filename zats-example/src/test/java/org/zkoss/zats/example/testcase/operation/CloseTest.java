package org.zkoss.zats.example.testcase.operation;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.CloseAgent;
import org.zkoss.zk.ui.Component;

public class CloseTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void testAgent() {
		DesktopAgent desktopAgent = autoClient.connect("/close.zul");

		ComponentAgent panel = desktopAgent.query("panel[title='closable']");
		panel.as(CloseAgent.class).close();
		Assert.assertNull(((Component)panel.getDelegatee()).getPage());
		
		ComponentAgent window = desktopAgent.query("window[title='closable']");
		window.as(CloseAgent.class).close();
		Assert.assertNull(((Component)window.getDelegatee()).getPage());
		
		ComponentAgent tab = desktopAgent.query("tab[label='closable']");
		tab.as(CloseAgent.class).close();
		Assert.assertNull(desktopAgent.query("tab[label='closable']"));
	}
}
