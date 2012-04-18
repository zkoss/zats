package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zul.Label;

public class ClickTest {
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
	public void test() {
		DesktopAgent desktop = Zats.newClient().connect("/click.zul");

		ComponentAgent label = desktop.query("#mylabel");
		ComponentAgent eventName = desktop.query("#eventName");
		
		label.click();
		assertEquals("onClick", eventName.as(Label.class).getValue());
		
		label.as(ClickAgent.class).doubleClick();
		assertEquals("onDoubleClick", eventName.as(Label.class).getValue());
		
		label.as(ClickAgent.class).rightClick();
		assertEquals("onRightClick", eventName.as(Label.class).getValue());
	}
}
