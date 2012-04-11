package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

public class HelloTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp"); // user can load by
													// configuration file
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
	public void test() {
		DesktopAgent desktop = Zats.newClient().connect("/hello.zul");

		ComponentAgent button = desktop.query("button");
		ComponentAgent label = desktop.query("label");
		
		//button.as(ClickAgent.class).click();
		button.click();
		assertEquals("Hello Mimic", label.as(Label.class).getValue());
	}
}
