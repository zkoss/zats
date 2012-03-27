package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zul.Label;

public class ClickAgentTest {

	@BeforeClass
	public static void init() {
		Conversations.start("."); // user can load by
														// configuration file
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
		Conversations.open("/~./basic/click.zul");
		assertEquals("Hello World!", Searcher.find("#msg").as(Label.class).getValue());
		Searcher.find("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", Searcher.find("#msg").as(Label.class).getValue());
	}
}
