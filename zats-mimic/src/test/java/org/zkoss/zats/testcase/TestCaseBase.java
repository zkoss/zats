package org.zkoss.zats.testcase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.zkoss.zats.mimic.Zats;

public class TestCaseBase
{
	@BeforeClass
	public static void init()
	{
		Zats.init("./src/test/resources");
	}

	@AfterClass
	public static void end()
	{
		Zats.end();
	}

	@After
	public void after()
	{
		Zats.cleanup();
	}

}
