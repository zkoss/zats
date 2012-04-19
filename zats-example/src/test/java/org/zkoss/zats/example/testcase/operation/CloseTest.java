package org.zkoss.zats.example.testcase.operation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.CloseAgent;
import org.zkoss.zk.ui.Component;

public class CloseTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp"); // user can load by
													// configuration file
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}

	@Test
	public void testAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/close.zul");
		
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
