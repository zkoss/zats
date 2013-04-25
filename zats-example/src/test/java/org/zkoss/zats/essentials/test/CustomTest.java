package org.zkoss.zats.essentials.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Zats;

public class CustomTest {
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
	public void test() throws Exception{
		/* pseudo code 
		DesktopAgent desktop = Zats.newClient().connect("/essentials/custom.zul");
		ComponentAgent mycomponent = desktop.query("mycomponent");
		AuData myEventData = new AuData("onMyEventName");
		myEventData.setData("mykey", "myvalue").setData("data", 10);
		mycomponent.as(AuAgent.class).post(myEventData);
		//verify result
		 */
	}
}

