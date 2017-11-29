package org.zkoss.zats.essentials.test;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

import java.util.Collections;

public class IncludeTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() throws Exception{
	    Client client = autoClient.getClient();
	    DesktopAgent desktop = client.connectAsIncluded("/essentials/included.zul", Collections.<String, Object>singletonMap("message", "Hello world!"));
	    Label msg = desktop.query("#msg").as(Label.class);
	    Assert.assertEquals("Hello world!", msg.getValue());
	}
}
