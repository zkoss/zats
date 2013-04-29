/* ZK5TestSuite.java

	Purpose:
		
	Description:
		
	History:
		2012/3/26 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase.ext6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zul.Label;

/**
 * @author dennis
 *
 */
public class MiscTest {
	@BeforeClass
	public static void init()
	{
//		Conversations.start("./src/test/resources/web");
		Zats.init(".");
	}

	@AfterClass
	public static void end()
	{
		Zats.end();
	}

	@After
	public void after()
	{
		Zats.cleanup();
	}
	
	@Test
	public void testVersion() {
		Assert.assertTrue(Util.isZKVersion(6));
	}
	
	@Test
	public void testConnectAsIncludedWithZK6Features1() {
		Map<String, Object> args = new HashMap<String, Object>();
		for (int i = 0; i < 10; ++i)
			args.put("k" + i, "v" + i);

		Client client = Zats.newClient();
		DesktopAgent desktop = client.connectAsIncluded("/~./basic/include-ext6.zul", args);
		Assert.assertNotNull(desktop.query("#content"));
		List<ComponentAgent> labels = desktop.queryAll("#content label");
		Assert.assertEquals(args.size(), labels.size());

		Map<String, Object> results = new HashMap<String, Object>();
		for (ComponentAgent label : labels) {
			String[] tokens = label.as(Label.class).getValue().split("=");
			results.put(tokens[0], tokens[1]);
		}
		Assert.assertEquals(args, results);

		// zkbind
		Assert.assertEquals(args.size() + 2, desktop.queryAll("label").size());
		Label msg = desktop.query("#msg").as(Label.class);
		Assert.assertEquals("Hello! ZK!", msg.getValue());
	}
	
	@Test
	public void testConnectAsIncludedWithZK6Features2() {

		Map<String, Object> args = new HashMap<String, Object>();
		for (int i = 0; i < 5; ++i)
			args.put("key" + i, "value" + i);

		Client client = Zats.newClient();
		DesktopAgent desktop = client.connect("/~./basic/include-outer-ext6.zul");
		Assert.assertNotNull(desktop.query("#inner #content"));
		List<ComponentAgent> labels = desktop.queryAll("#inner #content label");
		Assert.assertEquals(args.size(), labels.size());

		Map<String, Object> results = new HashMap<String, Object>();
		for (ComponentAgent label : labels) {
			String[] tokens = label.as(Label.class).getValue().split("=");
			results.put(tokens[0], tokens[1]);
		}
		Assert.assertEquals(args, results);

		// zkbind
		Assert.assertEquals(args.size() + 2, desktop.queryAll("#inner label").size());
		Label msg = desktop.query("#inner #msg").as(Label.class);
		Assert.assertEquals("Hello! ZK!", msg.getValue());

	}

}
