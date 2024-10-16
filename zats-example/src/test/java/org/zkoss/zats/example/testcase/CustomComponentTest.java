package org.zkoss.zats.example.testcase;

import org.junit.*;
import org.zkoss.zats.junit.*;
import org.zkoss.zats.mimic.*;

import static org.junit.Assert.assertNotNull;

public class CustomComponentTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() {
		DesktopAgent desktop = autoClient.connect("/custom-component.zul");

		ComponentAgent textbox = desktop.query("emailbox");
		assertNotNull(textbox);
	}
}
