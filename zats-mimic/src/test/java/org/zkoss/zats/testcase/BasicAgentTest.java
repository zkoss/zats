/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Stack;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treeitem;

/**
 * @author dennis
 *
 */
public class BasicAgentTest {
	@BeforeClass
	public static void init()
	{
//		Conversations.start("./src/test/resources");
		Conversations.start(".");
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
	public void testKeyStrokeAgent(){
		Conversations.open("/~./basic/keystroke.zul");
		
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
	
	@Test
	public void testTypeAgent1(){
		Conversations.open("/~./basic/type1.zul");
		
		ComponentAgent l = Searcher.find("#l1");
		ComponentAgent inp = Searcher.find("#inp1");
		//bandbox
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("A");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("B");
		Assert.assertEquals("B",l.as(Label.class).getValue());
		
		//combobox
		l = Searcher.find("#l2");
		inp = Searcher.find("#inp2");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("C");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("D");
		Assert.assertEquals("D",l.as(Label.class).getValue());
		
		
		//textbox
		l = Searcher.find("#l10");
		inp = Searcher.find("#inp10");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("E");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("F");
		Assert.assertEquals("F",l.as(Label.class).getValue());
		
		//decimalbox
		l = Searcher.find("#l4");
		inp = Searcher.find("#inp4");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());
		inp.type("-1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());

		inp.type("2.33");
		Assert.assertEquals("2.33",l.as(Label.class).getValue());
		
		//doublebox
		l = Searcher.find("#l5");
		inp = Searcher.find("#inp5");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("-3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("4.33");
		Assert.assertEquals("4.33",l.as(Label.class).getValue());
		
		//doublespinner
		l = Searcher.find("#l6");
		inp = Searcher.find("#inp6");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("-5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("6.33");
		Assert.assertEquals("6.33",l.as(Label.class).getValue());
		
		//intbox
		l = Searcher.find("#l7");
		inp = Searcher.find("#inp7");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("-7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("8");
		Assert.assertEquals("8",l.as(Label.class).getValue());
		
		
		
		
		//longbox
		l = Searcher.find("#l8");
		inp = Searcher.find("#inp8");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("-9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("10");
		Assert.assertEquals("10",l.as(Label.class).getValue());
		
		
		
		//spinner
		l = Searcher.find("#l9");
		inp = Searcher.find("#inp9");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("-11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("12");
		Assert.assertEquals("12",l.as(Label.class).getValue());
		
		//datebox
		l = Searcher.find("#l3");
		inp = Searcher.find("#inp3");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("20120223");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20110101");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20120320");
		Assert.assertEquals("20120320",l.as(Label.class).getValue());
		
		//timebox
		l = Searcher.find("#l11");
		inp = Searcher.find("#inp11");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("13:00");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("10:00");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("14:02");
		Assert.assertEquals("14:02",l.as(Label.class).getValue());
	}
	
	@Test
	public void testTypeAgent2(){
		Conversations.open("/~./basic/type2.zul");
		
		ComponentAgent l = Searcher.find("#l1");
		ComponentAgent inp = Searcher.find("#inp1");
		//bandbox
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("A");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("B");
		Assert.assertEquals("B",l.as(Label.class).getValue());
		
		//combobox
		l = Searcher.find("#l2");
		inp = Searcher.find("#inp2");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("C");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("D");
		Assert.assertEquals("D",l.as(Label.class).getValue());
		
		
		//textbox
		l = Searcher.find("#l10");
		inp = Searcher.find("#inp10");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("E");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("F");
		Assert.assertEquals("F",l.as(Label.class).getValue());
		
		//decimalbox
		l = Searcher.find("#l4");
		inp = Searcher.find("#inp4");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());
		inp.type("-1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());

		inp.type("2,222.33");
		Assert.assertEquals("2222.33",l.as(Label.class).getValue());
		
		//doublebox
		l = Searcher.find("#l5");
		inp = Searcher.find("#inp5");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("-3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("4,444.33");
		Assert.assertEquals("4444.33",l.as(Label.class).getValue());
		
		//doublespinner
		l = Searcher.find("#l6");
		inp = Searcher.find("#inp6");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("-5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("6,666.33");
		Assert.assertEquals("6666.33",l.as(Label.class).getValue());
		
		//intbox
		l = Searcher.find("#l7");
		inp = Searcher.find("#inp7");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("-7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("8,888");
		Assert.assertEquals("8888",l.as(Label.class).getValue());
		
		
		
		
		//longbox
		l = Searcher.find("#l8");
		inp = Searcher.find("#inp8");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("-9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("1,110");
		Assert.assertEquals("1110",l.as(Label.class).getValue());
		
		
		
		//spinner
		l = Searcher.find("#l9");
		inp = Searcher.find("#inp9");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("-11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("1,112");
		Assert.assertEquals("1112",l.as(Label.class).getValue());
		
		//datebox
		l = Searcher.find("#l3");
		inp = Searcher.find("#inp3");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("23022012");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("01012011");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20032012");
		Assert.assertEquals("20120320",l.as(Label.class).getValue());
		
		//timebox
		l = Searcher.find("#l11");
		inp = Searcher.find("#inp11");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("00:13");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("00:10");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("02:14");
		Assert.assertEquals("14:02",l.as(Label.class).getValue());
	}
	
	@Test
	public void testOpenAgentTree(){
		Conversations.open("/~./basic/open-tree.zul");
		
		ComponentAgent tree = Searcher.find("#tree");
		List<ComponentAgent> items = tree.findAll("treeitem");
		Assert.assertEquals(2, items.size());
		
		Stack<ComponentAgent> stack = new Stack<ComponentAgent>();
		stack.addAll(items);
		
		while(!stack.empty()){
			ComponentAgent item = stack.pop();
			
			if(item.find("treechildren")!=null){
				Assert.assertFalse(item.as(Treeitem.class).isOpen());
				items = item.find("treechildren").findAll("treeitem");//the sub-treeitem.
				Assert.assertEquals(0, items.size());
				
				item.as(OpenAgent.class).open(true);//trigger open to load the tree item.
				
				Assert.assertTrue(item.as(Treeitem.class).isOpen());
				items = item.find("treechildren").findAll("treeitem");//the sub-treeitem.
				Assert.assertEquals(2, items.size());
				for(ComponentAgent si:items){
					stack.push(si);
				}
			}
		}
		
		items = tree.findAll("treeitem");
		Assert.assertEquals(14, items.size());
	}
	
	@Test
	public void testFocusAgent() {
		Conversations.open("/~./basic/focus.zul");
		Label curr = Searcher.find("#current").as(Label.class);
		Label lost = Searcher.find("#lost").as(Label.class);
		assertTrue(curr.getValue().length() <= 0);
		assertTrue(curr.getValue().length() <= 0);

		for (int i = 1; i <= 11; ++i) {
			ComponentAgent comp = Searcher.find("#c" + i);
			comp.as(FocusAgent.class).focus();
			String name = comp.as(AbstractComponent.class).getDefinition().getName();
			assertEquals(name, curr.getValue());
			comp.as(FocusAgent.class).blur();
			assertEquals(name, lost.getValue());
		}
	}

	@Test
	public void testCheckAgent() {
		Conversations.open("/~./basic/check.zul");

		// validate msg
		Label msg = Searcher.find("#msg").as(Label.class);
		assertTrue(msg.getValue().length() <= 0);

		// test checkbox and menuitem
		String label = "";
		for (int i = 1; i <= 6; ++i) {
			Searcher.find("#c" + i).as(CheckAgent.class).check(true);
			label += "c" + i + " ";
			assertEquals(label, msg.getValue());
		}
		// test radiogroup
		for (int i = 7; i <= 9; ++i) {
			Searcher.find("#c" + i).as(CheckAgent.class).check(true);
			assertEquals(label + "c" + i + " ", msg.getValue());
		}
	}

	@Test
	public void testClickAgent() {
		Conversations.open("/~./basic/click.zul");
		assertEquals("Hello World!", Searcher.find("#msg").as(Label.class).getValue());
		Searcher.find("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", Searcher.find("#msg").as(Label.class).getValue());
	}
}
