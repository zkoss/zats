/* ConversationTest.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zul.Label;

/**
 * this is for local test, don't include it in other project
 * @author dennis
 *
 */
public class ConversationTest {
	@BeforeClass
	public static void init()
	{
		Conversations.start("./src/test/resources/web");
	}

	@AfterClass
	public static void end()
	{
		Conversations.stop();
	}

	@After
	public void after()
	{
		Conversations.clean();
	}

	@Test
	public void testLoadLocal() {
		//to test open a local zul
		DesktopAgent desktop = Conversations.open("/basic/click.zul");
		assertEquals("Hello World!", desktop.query("#msg").as(Label.class).getValue());
		desktop.query("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", desktop.query("#msg").as(Label.class).getValue());
	}
}
