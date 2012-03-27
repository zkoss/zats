package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zul.Label;

public class FocusAgentTest {
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
		Conversations.open("/~./basic/focus.zul");
		Label curr = Searcher.find("#current").as(Label.class);
		Label lost = Searcher.find("#lost").as(Label.class);
		assertTrue(curr.getValue().length() <= 0);
		assertTrue(curr.getValue().length() <= 0);

		for (int i = 1; i <= 11; ++i) {
			ComponentAgent comp = Searcher.find("#c" + i);
			comp.as(FocusAgent.class).focus();
			String name = comp.as(AbstractComponent.class).getDefinition().getName();
			assertEquals(name, curr.getValue());
			comp.as(FocusAgent.class).blur();
			assertEquals(name, lost.getValue());
		}
	}
}
