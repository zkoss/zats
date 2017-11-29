package org.zkoss.zats.essentials.test;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;

public class CookieTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() throws Exception {
		Client client = autoClient.getClient();
		DesktopAgent desktop = client.connect("/essentials/cookie.zul");
		Assert.assertEquals("bar", client.getCookie("foo"));
		Assert.assertEquals(null, client.getCookie("not existed"));
		desktop.query("#change").click();
		Assert.assertEquals("hello", client.getCookie("foo"));
	}


}
