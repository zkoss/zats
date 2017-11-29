package org.zkoss.zats.example.testcase.operation;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zul.Label;

public class ClickTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() {
		DesktopAgent desktop = autoClient.connect("/click.zul");

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
