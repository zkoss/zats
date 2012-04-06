/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		Mar 27, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.testcase.ext6;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

/**
 * @author pao
 * 
 */
public class BasicAgentTest {
	@BeforeClass
	public static void init() {
		Conversations.start(".");
	}

	@AfterClass
	public static void end() {
		Conversations.stop();
	}

	@After
	public void after() {
		Conversations.closeAll();
	}

	@Test
	public void testToolbarButtonCheck() {
		DesktopAgent desktop = Conversations.open().connect("/~./basic/toolbar.zul");

		for (int i = 1; i <= 6; ++i)
			assertEquals("tbb" + i, desktop.query("#tbb" + i).as(Toolbarbutton.class).getLabel());

		Label clicked = desktop.query("#clicked").as(Label.class);
		assertEquals("", clicked.getValue());

		for (int i = 1; i <= 6; ++i) {
			desktop.query("#tbb" + i).click();
			assertEquals("tbb" + i, clicked.getValue());
		}

		Label checked = desktop.query("#checked").as(Label.class);
		assertEquals("", checked.getValue());

		String[] values = { "tbb4 ", "tbb4 tbb5 ", "tbb4 tbb5 tbb6 ", "tbb4 tbb6 " };
		for (int i = 4; i <= 6; ++i) {
			desktop.query("#tbb" + i).check(true);
			assertEquals(values[i - 4], checked.getValue());
			assertEquals("tbb6", clicked.getValue()); // "check" should not perform "click"
		}

		desktop.query("#tbb5").check(false);
		assertEquals(values[3], checked.getValue());
	}
}
