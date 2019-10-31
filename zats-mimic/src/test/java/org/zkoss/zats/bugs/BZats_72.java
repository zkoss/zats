package org.zkoss.zats.bugs;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;

public class ZatsBlockingAfterError {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment(".");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test(expected = ZatsException.class)
	public void testMultiplePagesWithError() {
		DesktopAgent desktopAgent;
		Client client = autoClient.getClient();
		desktopAgent = client.connectWithContent("<zk>1</zk>", "zul", null, null);
		desktopAgent = client.connectWithContent("<zk><parse error</zk>", "zul", null, null);
		desktopAgent = client.connectWithContent("<zk>2</zk>", "zul", null, null);
	}
}
