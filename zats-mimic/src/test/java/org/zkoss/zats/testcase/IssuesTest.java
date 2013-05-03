/* IssuesTest.java

	Purpose:
		
	Description:
		
	History:
		Apr 29, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
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
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./issue/zats-15.zul");

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
}
