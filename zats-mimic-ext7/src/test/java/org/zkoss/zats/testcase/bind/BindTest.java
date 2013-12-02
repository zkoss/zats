package org.zkoss.zats.testcase.bind;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Selectbox;

/**
 * test case for bugs from number 500-999
 * @author dennis
 *
 */
public class BindTest{

	
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
	public void b00810ListboxMultiple(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/B00810ListboxMultiple.zul"); 
		
		ComponentAgent listbox1 = desktop.query("#listbox1");
		ComponentAgent listbox2 = desktop.query("#listbox2");
		ComponentAgent listbox3 = desktop.query("#listbox3");
		ComponentAgent l1 = desktop.query("#l1");
		ComponentAgent toggle = desktop.query("#toggle");
		 
		desktop.queryAll("#listbox1 > listitem").get(1).as(MultipleSelectAgent.class).select();
		
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox1));
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox2));
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox3));
		Assert.assertEquals("[1]", l1.as(Label.class).getValue());
		
		desktop.queryAll("#listbox2 > listitem").get(3).as(MultipleSelectAgent.class).select();
		Assert.assertArrayEquals(new long[]{1,3}, ListboxUtil.getSelectedIndexs(listbox1));
		Assert.assertArrayEquals(new long[]{1,3}, ListboxUtil.getSelectedIndexs(listbox2));
		Assert.assertArrayEquals(new long[]{1,3}, ListboxUtil.getSelectedIndexs(listbox3));
		Assert.assertEquals("[1, 3]", l1.as(Label.class).getValue());

		desktop.queryAll("#listbox3 > listitem").get(6).as(MultipleSelectAgent.class).select();
		
		Assert.assertArrayEquals(new long[]{1,3,6}, ListboxUtil.getSelectedIndexs(listbox1));
		Assert.assertArrayEquals(new long[]{1,3,6}, ListboxUtil.getSelectedIndexs(listbox2));
		Assert.assertArrayEquals(new long[]{1,3,6}, ListboxUtil.getSelectedIndexs(listbox3));
		Assert.assertEquals("[1, 3, 6]", l1.as(Label.class).getValue());
		
		toggle.as(ClickAgent.class).click();
//		listbox3.as(RendererAgent.class).render(-1, -1);
		desktop.queryAll("#listbox3 > listitem").get(7).as(SelectAgent.class).select();
		Assert.assertArrayEquals(new long[]{7}, ListboxUtil.getSelectedIndexs(listbox1));
		Assert.assertArrayEquals(new long[]{7}, ListboxUtil.getSelectedIndexs(listbox2));
		Assert.assertArrayEquals(new long[]{7}, ListboxUtil.getSelectedIndexs(listbox3));
		Assert.assertEquals("[7]", l1.as(Label.class).getValue());
		
		desktop.queryAll("#listbox3 > listitem").get(1).as(SelectAgent.class).select();
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox1));
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox2));
		Assert.assertArrayEquals(new long[]{1}, ListboxUtil.getSelectedIndexs(listbox3));
		Assert.assertEquals("[1]", l1.as(Label.class).getValue());
		
	}
	
	@Test
	public void b00821SelectedIndex(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/B00821SelectedIndex.zul");
		
		ComponentAgent selectbox = desktop.query("#selectbox");
		ComponentAgent listbox = desktop.query("#listbox");
		ComponentAgent combobox = desktop.query("#combobox");
		ComponentAgent i1 = desktop.query("#i1");
		
		i1.type("1");
		
		Assert.assertEquals(1L, ListboxUtil.getSelectedIndex(listbox));
		Assert.assertEquals(1L, selectbox.as(Selectbox.class).getSelectedIndex());
		Assert.assertEquals("B", combobox.as(Combobox.class).getValue());
		
		i1.type("2");
		Assert.assertEquals(2L, ListboxUtil.getSelectedIndex(listbox));
		Assert.assertEquals(2L, selectbox.as(Selectbox.class).getSelectedIndex());
		Assert.assertEquals("C", combobox.as(Combobox.class).getValue());
	}
	
	
	
	@Test
	public void b00878WrongValueException2(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/B00878WrongValueException2.zul");
		
		ComponentAgent l = desktop.query("#l1");
		ComponentAgent inp = desktop.query("#inp1");
		//bandbox
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("A");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("B");
		Assert.assertEquals("B",l.as(Label.class).getValue());
		
		//combobox
		l = desktop.query("#l2");
		inp = desktop.query("#inp2");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("C");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("D");
		Assert.assertEquals("D",l.as(Label.class).getValue());
		
		
		//textbox
		l = desktop.query("#l10");
		inp = desktop.query("#inp10");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("E");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("F");
		Assert.assertEquals("F",l.as(Label.class).getValue());
		
		//decimalbox
		l = desktop.query("#l4");
		inp = desktop.query("#inp4");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());
		inp.type("-1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());

		inp.type("2.33");
		Assert.assertEquals("2.33",l.as(Label.class).getValue());
		
		//doublebox
		l = desktop.query("#l5");
		inp = desktop.query("#inp5");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("-3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("4.33");
		Assert.assertEquals("4.33",l.as(Label.class).getValue());
		
		//doublespinner
		l = desktop.query("#l6");
		inp = desktop.query("#inp6");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("-5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("6.33");
		Assert.assertEquals("6.33",l.as(Label.class).getValue());
		
		//intbox
		l = desktop.query("#l7");
		inp = desktop.query("#inp7");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("-7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("8");
		Assert.assertEquals("8",l.as(Label.class).getValue());
		
		
		
		
		//longbox
		l = desktop.query("#l8");
		inp = desktop.query("#inp8");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("-9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("10");
		Assert.assertEquals("10",l.as(Label.class).getValue());
		
		
		
		//spinner
		l = desktop.query("#l9");
		inp = desktop.query("#inp9");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("-11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("12");
		Assert.assertEquals("12",l.as(Label.class).getValue());
		
		//datebox
		l = desktop.query("#l3");
		inp = desktop.query("#inp3");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("20120223");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20110101");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20120320");
		Assert.assertEquals("20120320",l.as(Label.class).getValue());
		
		//timebox
		l = desktop.query("#l11");
		inp = desktop.query("#inp11");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("13:00");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("10:00");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("14:02");
		Assert.assertEquals("14:02",l.as(Label.class).getValue());
	}
	
	@Test
	public void b00878WrongValueException3(){
		DesktopAgent desktop = Zats.newClient().connect("/~./bind/B00878WrongValueException3.zul");
		
		ComponentAgent l = desktop.query("#l1");
		ComponentAgent inp = desktop.query("#inp1");
		//bandbox
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("A");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("");
		Assert.assertEquals("A",l.as(Label.class).getValue());
		
		inp.type("B");
		Assert.assertEquals("B",l.as(Label.class).getValue());
		
		//combobox
		l = desktop.query("#l2");
		inp = desktop.query("#inp2");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("C");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("C",l.as(Label.class).getValue());
		inp.type("D");
		Assert.assertEquals("D",l.as(Label.class).getValue());
		
		
		//textbox
		l = desktop.query("#l10");
		inp = desktop.query("#inp10");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("E");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("");
		Assert.assertEquals("E",l.as(Label.class).getValue());
		inp.type("F");
		Assert.assertEquals("F",l.as(Label.class).getValue());
		
		//decimalbox
		l = desktop.query("#l4");
		inp = desktop.query("#inp4");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());
		inp.type("-1");
		Assert.assertEquals("1.0",l.as(Label.class).getValue());

		inp.type("2,222.33");
		Assert.assertEquals("2222.33",l.as(Label.class).getValue());
		
		//doublebox
		l = desktop.query("#l5");
		inp = desktop.query("#inp5");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("-3");
		Assert.assertEquals("3.0",l.as(Label.class).getValue());
		inp.type("4,444.33");
		Assert.assertEquals("4444.33",l.as(Label.class).getValue());
		
		//doublespinner
		l = desktop.query("#l6");
		inp = desktop.query("#inp6");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("-5");
		Assert.assertEquals("5.0",l.as(Label.class).getValue());

		inp.type("6,666.33");
		Assert.assertEquals("6666.33",l.as(Label.class).getValue());
		
		//intbox
		l = desktop.query("#l7");
		inp = desktop.query("#inp7");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("-7");
		Assert.assertEquals("7",l.as(Label.class).getValue());
		inp.type("8,888");
		Assert.assertEquals("8888",l.as(Label.class).getValue());
		
		
		
		
		//longbox
		l = desktop.query("#l8");
		inp = desktop.query("#inp8");
		Assert.assertEquals("",l.as(Label.class).getValue());
		
		inp.type("9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("-9");
		Assert.assertEquals("9",l.as(Label.class).getValue());
		inp.type("1,110");
		Assert.assertEquals("1110",l.as(Label.class).getValue());
		
		
		
		//spinner
		l = desktop.query("#l9");
		inp = desktop.query("#inp9");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("-11");
		Assert.assertEquals("11",l.as(Label.class).getValue());
		inp.type("1,112");
		Assert.assertEquals("1112",l.as(Label.class).getValue());
		
		//datebox
		l = desktop.query("#l3");
		inp = desktop.query("#inp3");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("23022012");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("01012011");
		Assert.assertEquals("20120223",l.as(Label.class).getValue());
		inp.type("20032012");
		Assert.assertEquals("20120320",l.as(Label.class).getValue());
		
		//timebox
		l = desktop.query("#l11");
		inp = desktop.query("#inp11");
		Assert.assertEquals("",l.as(Label.class).getValue());
		inp.type("00:13");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("00:10");
		Assert.assertEquals("13:00",l.as(Label.class).getValue());
		inp.type("02:14");
		Assert.assertEquals("14:02",l.as(Label.class).getValue());
	}
}
