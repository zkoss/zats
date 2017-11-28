package org.zkoss.zats.junit;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.lang.Library;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

public class AutoEnvClientWebInfTest {
	@ClassRule
	public static AutoEnvironment autoEnv = new AutoEnvironment("src/test/resources/web/WEB-INF/custom", "src/test/resources/web/junit");

	@Rule
	public AutoClient autoClient = autoEnv.autoClient();

	@Test
	public void testAutoClient() {
		DesktopAgent desktopAgent = autoClient.connect("/testLibraryProperty.zul");
		Label testLabel = desktopAgent.query("#libProp").as(Label.class);
		Assert.assertEquals("libproperty ('zats.hello') from web/WEB-INF/custom/zk.xml: ", "hello zats", testLabel.getValue());
		Library.setProperty("zats.hello", null);
	}

}
