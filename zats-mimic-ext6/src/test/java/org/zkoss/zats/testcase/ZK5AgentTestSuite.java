/* ZK5TestSuite.java

	Purpose:
		
	Description:
		
	History:
		2012/3/26 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author dennis
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ClickAgentTest.class,BasicAgentTest.class,CheckAgentTest.class,FocusAgentTest.class})
public class ZK5AgentTestSuite {

}
