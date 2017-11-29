package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

public class HelloTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() {
		DesktopAgent desktop = autoClient.connect("/hello.zul");

		ComponentAgent button = desktop.query("button");
		ComponentAgent label = desktop.query("label");
		
		//button.as(ClickAgent.class).click();
		button.click();
		assertEquals("Hello Mimic", label.as(Label.class).getValue());
	}
}
