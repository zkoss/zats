package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zul.Label;

public class CheckAgentTest {
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
	public void test() {
		Conversations.open("/~./basic/check.zul");

		// validate msg
		Label msg = Searcher.find("#msg").as(Label.class);
		assertTrue(msg.getValue().length() <= 0);

		// test checkbox and menuitem
		String label = "";
		for (int i = 1; i <= 6; ++i) {
			Searcher.find("#c" + i).as(CheckAgent.class).check(true);
			label += "c" + i + " ";
			assertEquals(label, msg.getValue());
		}
		// test radiogroup
		for (int i = 7; i <= 9; ++i) {
			Searcher.find("#c" + i).as(CheckAgent.class).check(true);
			assertEquals(label + "c" + i + " ", msg.getValue());
		}
	}
}
