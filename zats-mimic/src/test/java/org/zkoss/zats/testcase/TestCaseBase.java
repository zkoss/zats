package org.zkoss.zats.testcase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.zkoss.zats.mimic.Conversations;

public class TestCaseBase
{
	@BeforeClass
	public static void init()
	{
		Conversations.start("./src/test/resources");
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

}
