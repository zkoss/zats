/* ConversationTest.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

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
		Conversations.closeAll();
	}

	@Test
	public void testLoadLocal() {
		//to test open a local zul
		DesktopAgent desktop = Conversations.open().connect("/basic/click.zul");
		assertEquals("Hello World!", desktop.query("#msg").as(Label.class).getValue());
		desktop.query("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", desktop.query("#msg").as(Label.class).getValue());
	}
	
	@Test
	public void test2Desktops(){
		DesktopAgent desktop1 = Conversations.open().connect("/basic/click.zul");
		DesktopAgent desktop2 = Conversations.open().connect("/basic/click.zul");
		assertNotSame(desktop1, desktop2);
		
		assertEquals("Hello World!", desktop1.query("#msg").as(Label.class).getValue());
		desktop1.query("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", desktop1.query("#msg").as(Label.class).getValue());
		
		assertEquals("Hello World!", desktop2.query("#msg").as(Label.class).getValue());
		desktop2.query("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", desktop2.query("#msg").as(Label.class).getValue());
	}

}
