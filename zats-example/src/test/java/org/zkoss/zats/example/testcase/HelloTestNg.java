package org.zkoss.zats.example.testcase;

//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//import org.zkoss.zats.mimic.ComponentAgent;
//import org.zkoss.zats.mimic.DesktopAgent;
//import org.zkoss.zats.mimic.Zats;
//import org.zkoss.zul.Label;


/**
 * to run this test case enable the maven dependency for testNG in the pom.xml
 */
public class HelloTestNg {
//	@BeforeClass
//	public static void init() {
//		Zats.init("./src/main/webapp");
//	}
//
//	@AfterClass
//	public static void end() {
//		Zats.end();
//	}
//
//	@AfterMethod
//	public void after() {
//		Zats.cleanup();
//	}
//
//	@Test
//	public void test() {
//		DesktopAgent desktop = Zats.newClient().connect("/hello.zul");
//
//		ComponentAgent button = desktop.query("button");
//		ComponentAgent label = desktop.query("label");
//
//		//button.as(ClickAgent.class).click();
//		button.click();
//		Assert.assertEquals("Hello Mimic", label.as(Label.class).getValue());
//	}
}
