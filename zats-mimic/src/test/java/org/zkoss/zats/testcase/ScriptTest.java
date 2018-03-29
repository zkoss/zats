package org.zkoss.zats.testcase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zul.Listcell;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScriptTest {

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
	public void testSyntaxError() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/script.zul");
		assertTrue(true);
	}


}
