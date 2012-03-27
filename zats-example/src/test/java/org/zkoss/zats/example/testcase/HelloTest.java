package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zul.Label;

public class HelloTest {
	@BeforeClass
	public static void init() {
		Conversations.start("./src/main/webapp"); // user can load by
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
		Conversations.open("/hello.zul");

		ComponentAgent button = Searcher.find("button");
		ComponentAgent label = Searcher.find("label");
		
		//button.as(ClickAgent.class).click();
		button.click();
		assertEquals("Hello Mimic", label.as(Label.class).getValue());
	}
}
