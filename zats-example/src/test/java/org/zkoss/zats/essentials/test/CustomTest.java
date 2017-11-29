package org.zkoss.zats.essentials.test;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.Zats;

public class CustomTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

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

