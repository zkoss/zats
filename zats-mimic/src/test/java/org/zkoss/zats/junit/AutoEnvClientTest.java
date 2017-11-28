package org.zkoss.zats.junit;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;


public class AutoEnvClientTest {
	@ClassRule
	public static AutoEnvironment autoEnv = new AutoEnvironment("src/test/resources/web/junit");

	@Rule
	public AutoClient autoClient = autoEnv.autoClient();

	@Test
	public void testAutoClient() {
		DesktopAgent desktopAgent = autoClient.connect("/test.zul");
		Label testLabel = desktopAgent.query("#testLabel").as(Label.class);
		Assert.assertEquals("initial label: ", "initial", testLabel.getValue());
		desktopAgent.query("#testButton").click();
		Assert.assertEquals("updated label: ", "updated", testLabel.getValue());
	}

	@Test
	public void testLibraryProperty() {
		DesktopAgent desktopAgent = autoClient.connect("/testLibraryProperty.zul");
		Label testLabel = desktopAgent.query("#libProp").as(Label.class);
		Assert.assertEquals("libproperty ('zats.hello') should not exist in default config: ", "", testLabel.getValue());
	}
}
