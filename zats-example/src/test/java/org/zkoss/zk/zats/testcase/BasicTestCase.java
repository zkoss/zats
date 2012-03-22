/* KeyStroke.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zk.zats.testcase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zul.Label;

/**
 * @author dennis
 *
 */
public class BasicTestCase {
	@BeforeClass
	public static void init()
	{
		Conversations.start("./src/main/webapp");
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
	public void testKeyStroke(){
		Conversations.open("/basic/keystroke.zul");
		
		ComponentAgent inp1 = Searcher.find("#inp1");
		ComponentAgent inp2 = Searcher.find("#inp2");
		ComponentAgent l1 = Searcher.find("#l1");
		
		Assert.assertEquals("", l1.as(Label.class).getValue());
		
		inp1.stroke("#enter");
		Assert.assertEquals("ENTER key is pressed", l1.as(Label.class).getValue());
		
		inp1.stroke("#esc");
		Assert.assertEquals("ESC key is pressed", l1.as(Label.class).getValue());
		
		inp1.stroke("^a");
		Assert.assertEquals("Ctrl+A is pressed,alt:false,ctrl:true,shift:false", l1.as(Label.class).getValue());
		
		inp1.stroke("@b");
		Assert.assertEquals("Alt+B is pressed,alt:true,ctrl:false,shift:false", l1.as(Label.class).getValue());
		
		inp1.stroke("#f8");
		Assert.assertEquals("F8 is pressed,alt:false,ctrl:false,shift:false", l1.as(Label.class).getValue());
		
		
		inp2.stroke("#right");
		Assert.assertEquals("keyCode:39 is pressed,alt:false,ctrl:false,shift:false", l1.as(Label.class).getValue());
		
		inp2.as(KeyStrokeAgent.class).stroke("$#left");
		Assert.assertEquals("keyCode:37 is pressed,alt:false,ctrl:false,shift:true", l1.as(Label.class).getValue());
		
		try{
			inp1.as(KeyStrokeAgent.class).stroke("^a#right");//2 key
			Assert.fail("should not go here");
		}catch(AgentException x){}
		
		try{
			inp1.as(KeyStrokeAgent.class).stroke("^");//no keycode
			Assert.fail("should not go here");
		}catch(AgentException x){}
		
		
	}
}
