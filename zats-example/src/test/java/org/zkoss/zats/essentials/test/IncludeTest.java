package org.zkoss.zats.essentials.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zul.Label;

public class IncludeTest {
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
	    Map<String, Object> args = new HashMap<String, Object>();
	    args.put("message", "Hello world!");
	    Client client = Zats.newClient();
	    DesktopAgent desktop = client.connectAsIncluded("/essentials/included.zul", args);
	    Label msg = desktop.query("#msg").as(Label.class);
	    Assert.assertEquals("Hello world!", msg.getValue());
	}



}
