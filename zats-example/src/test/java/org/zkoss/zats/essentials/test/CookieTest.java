package org.zkoss.zats.essentials.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;

public class CookieTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp");
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
	public void test() throws Exception{
		Client client = Zats.newClient();
		DesktopAgent desktop = client.connect("/essentials/cookie.zul");
		Assert.assertEquals("bar", client.getCookie("foo"));
		Assert.assertEquals(null, client.getCookie("not existed"));
		desktop.query("#change").click();
		Assert.assertEquals("hello", client.getCookie("foo"));
	}



}
