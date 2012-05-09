/* ZK5TestSuite.java

	Purpose:
		
	Description:
		
	History:
		2012/3/26 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase.ext6;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.impl.Util;

/**
 * @author dennis
 *
 */
public class MiscTest {
	@BeforeClass
	public static void init()
	{
//		Conversations.start("./src/test/resources/web");
		Zats.init(".");
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
	
	@Test
	public void testVersion() {
		Assert.assertTrue(Util.isZKVersion(6));
	}
}
