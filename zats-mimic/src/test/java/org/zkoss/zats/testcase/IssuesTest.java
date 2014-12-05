/* IssuesTest.java

	Purpose:
		
	Description:
		
	History:
		Apr 29, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.EchoEventMode;
import org.zkoss.zats.mimic.PageAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

/**
 * Issues from JIRA
 * @author pao
 */
public class IssuesTest {

	@BeforeClass
	public static void init() {
		Zats.init(".");
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
	public void testZATS15() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/15-pseudo.zul");

		assertEquals(5, desktopAgent.queryAll("#lb > listitem").size());
		assertEquals(1, desktopAgent.queryAll("#lb > listhead:first-child").size());
		assertEquals(0, desktopAgent.queryAll("#lb > listitem:first-child").size());
		
		List<ComponentAgent> comps = desktopAgent.queryAll("#lb > listitem > listcell:first-child");
		assertEquals(5, comps.size());

		comps = desktopAgent.queryAll("#lb > listitem:nth-child(2) > listcell");
		assertEquals(1, comps.size());
		assertEquals("item0", comps.get(0).as(Listcell.class).getLabel());
		
		ComponentAgent comp = desktopAgent.query("#lb > listitem > listcell:first-child");
		assertEquals("item0", comp.as(Listcell.class).getLabel());
		
		// without <listhead>
		comps = desktopAgent.queryAll("#nohead > listitem:first-child > listcell");
		assertEquals(1, comps.size());
		assertEquals("i0", comps.get(0).as(Listcell.class).getLabel());
		
	}
	
	@Test
	public void testZATS7() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/7-style.zul");
		
		Assert.assertNotNull(desktopAgent.query("#searchWin #searchButton"));
	}
	
	@Test 
	public void testZATS20() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/20-empty.zul");
		
		desktopAgent.query("#btn").click();
		Assert.assertNull(desktopAgent.query("#btn"));
		
		List<PageAgent> pages = desktopAgent.getPages();
		Assert.assertEquals(1, pages.size());
		Assert.assertNull(pages.get(0).query("#btn"));
	}
	
	@Test
	public void testZATS25() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/25-client.zul");

		ComponentAgent agent = desktopAgent.query("button");
		Assert.assertNotNull(agent);

	}
	
	@Test
	public void testZATS25withEchoEvent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/25-client-echo.zul");

		// echo events at doAfterComposer()
		ComponentAgent lblX = desktopAgent.query("#lblX");
		assertNotNull(lblX);
		assertEquals("Bar", lblX.as(Label.class).getValue());
		ComponentAgent lblY = desktopAgent.query("#lblY");
		assertNotNull(lblY);
		assertEquals("Bar2", lblY.as(Label.class).getValue());

		// immediate echo events
		Label lbl11 = desktopAgent.query("#lbl11").as(Label.class);
		Label lbl12 = desktopAgent.query("#lbl12").as(Label.class);
		Label lbl13 = desktopAgent.query("#lbl13").as(Label.class);
		assertEquals("", lbl11.getValue());
		assertEquals("", lbl12.getValue());
		assertEquals("", lbl13.getValue());
		assertFalse("incorrect".equals(lbl11.getValue()));

		ComponentAgent btn1 = desktopAgent.query("#btn1");
		btn1.click();
		assertEquals("MyEcho", lbl11.getValue());
		assertEquals("YourEcho", lbl12.getValue());
		assertEquals("ItsEcho", lbl13.getValue());
		btn1.click();
		assertEquals("MyEchoMyEcho", lbl11.getValue());
		assertEquals("YourEchoYourEcho", lbl12.getValue());
		assertEquals("ItsEchoItsEcho", lbl13.getValue());

		// immediate echo events without data 
		Label lbl21 = desktopAgent.query("#lbl21").as(Label.class);
		Label lbl22 = desktopAgent.query("#lbl22").as(Label.class);
		Label lbl23 = desktopAgent.query("#lbl23").as(Label.class);
		assertEquals("", lbl21.getValue());

		ComponentAgent btn2 = desktopAgent.query("#btn2");
		btn2.click();
		assertEquals("MyEcho2", lbl21.getValue());
		assertEquals("YourEcho2", lbl22.getValue());
		assertEquals("ItsEcho2", lbl23.getValue());
		btn2.click();
		assertEquals("MyEcho2MyEcho2", lbl21.getValue());
		assertEquals("YourEcho2YourEcho2", lbl22.getValue());
		assertEquals("ItsEcho2ItsEcho2", lbl23.getValue());

		// loop echo with normal operations - immediate mode
		Label lbl31 = desktopAgent.query("#lbl31").as(Label.class);
		Label lbl32 = desktopAgent.query("#lbl32").as(Label.class);
		Label lbl4 = desktopAgent.query("#lbl4").as(Label.class);
		assertEquals("", lbl31.getValue());
		assertEquals("", lbl32.getValue());
		assertEquals("", lbl4.getValue());

		desktopAgent.query("#btn3").click();
		assertEquals("3", lbl31.getValue());
		assertEquals("4", lbl32.getValue());
		assertEquals("", lbl4.getValue());
		desktopAgent.query("#btn4").click();
		assertEquals("3", lbl31.getValue());
		assertEquals("4", lbl32.getValue());
		assertEquals("HelloEcho", lbl4.getValue());

		// loop echo with normal operations - piggyback mode
		desktopAgent.getClient().setEchoEventMode(EchoEventMode.PIGGYBACK);

		String hellos = "HelloEcho";
		desktopAgent.query("#btn3").click();
		assertEquals("0", lbl31.getValue());
		assertEquals("0", lbl32.getValue());
		assertEquals(hellos, lbl4.getValue());

		String[] a31 = { "1", "2", "3", "3", "3", "3" };
		String[] a32 = { "1", "2", "3", "4", "4", "4" };
		for (int i = 0; i < a31.length; ++i) {
			desktopAgent.query("#btn4").click();
			assertEquals(a31[i], lbl31.getValue());
			assertEquals(a32[i], lbl32.getValue());
			assertEquals(hellos += "HelloEcho", lbl4.getValue());
		}
	}
	
	@Test
	public void testZATS34() {
		try {
			Logger logger = Logger.getLogger("org.zkoss.zats");
			logger.addHandler(new Handler() {
				public void publish(LogRecord record) {
					Throwable t = record.getThrown();
					if (t != null) {
						t.printStackTrace();
						Assert.fail();
					}
				}
				public void flush() {
				}
				public void close() throws SecurityException {
				}
			});

			DesktopAgent desktop = Zats.newClient().connect("/~./issue/34-client.zul");
			desktop.query("#win #btn").as(ClickAgent.class).click();
			Label label = desktop.query("#win #msg").as(Label.class);
			Assert.assertNotNull(label);
			Assert.assertEquals("test", label.getValue());
			
			desktop.query("#btn3").click();
			Assert.assertEquals("loaded",desktop.query("#msg2").as(Label.class).getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testZATS44() {
		DesktopAgent desktopAgent = Zats.newClient().connectWithContent("<div><label><![CDATA[<Div yM5Q2>]]></label></div>", null, null, null);
		assertEquals(1, desktopAgent.queryAll("div").size());
	}
}
