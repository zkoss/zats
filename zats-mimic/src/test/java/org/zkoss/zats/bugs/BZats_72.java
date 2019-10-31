package org.zkoss.zats.bugs;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.Client;

/**
 * https://tracker.zkoss.org/browse/ZATS-72
 */
public class BZats_72 {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment(".");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test(expected = ZatsException.class)
	public void testMultiplePagesWithError() {
		Client client = autoClient.getClient();
		client.connectWithContent("<zk>1</zk>", "zul", null, null);
		client.connectWithContent("<zk><parse error</zk>", "zul", null, null);
		client.connectWithContent("<zk>2</zk>", "zul", null, null);
	}
}
