/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		Mar 27, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
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
		Conversations.clean();
	}

	@Test
	public void testToolbarButtonCheck() {
		Conversations.open("/~./basic/toolbar.zul");

		for (int i = 1; i <= 6; ++i)
			assertEquals("tbb" + i, Conversations.query("#tbb" + i).as(Toolbarbutton.class).getLabel());

		Label clicked = Conversations.query("#clicked").as(Label.class);
		assertEquals("", clicked.getValue());

		for (int i = 1; i <= 6; ++i) {
			Conversations.query("#tbb" + i).click();
			assertEquals("tbb" + i, clicked.getValue());
		}

		Label checked = Conversations.query("#checked").as(Label.class);
		assertEquals("", checked.getValue());

		String[] values = { "tbb4 ", "tbb4 tbb5 ", "tbb4 tbb5 tbb6 ", "tbb4 tbb6 " };
		for (int i = 4; i <= 6; ++i) {
			Conversations.query("#tbb" + i).check(true);
			assertEquals(values[i - 4], checked.getValue());
			assertEquals("tbb6", clicked.getValue()); // "check" should not perform "click"
		}

		Conversations.query("#tbb5").check(false);
		assertEquals(values[3], checked.getValue());
	}
}
