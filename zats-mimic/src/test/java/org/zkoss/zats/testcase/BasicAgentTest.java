/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.util.media.ContentTypes;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DefaultZatsEnvironment;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.EchoEventMode;
import org.zkoss.zats.mimic.Resource;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zats.mimic.impl.operation.AbstractUploadAgentBuilder;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.HoverAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.SizeAgent;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zats.mimic.operation.RenderAgent;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Slider;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Vbox;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.Component;

/**
 * 
 * @author pao
 */
public class BasicAgentTest {
	private static Logger logger = Logger.getLogger(BasicAgentTest.class.getName());

	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			String s = Integer.toHexString(b & 0xff);
			if (s.length() == 1)
				sb.append('0');
			sb.append(s);
		}
		return sb.toString();
	}

	private static final String[] componentNames = { "a", "button", "captcha", "fileupload", "label", "menubar", "menu", "menuitem", "menuseparator",
			"popup", "progressmeter", "separator", "space", "toolbarbutton", "bandbox", "colorbox",
			"combobox", "comboitem", "datebox", "decimalbox", "doublebox", "doublespinner", "intbox", "longbox",
			"spinner", "textbox", "timebox", "checkbox", "radio", "slider", "caption", "div",
			"groupbox", "panel", "span", "tab", "window", "grid", "detail", "group", "listitem", "listgroup", "treeitem" };

	@BeforeClass
	public static void init() {
		Zats.init(".");
	}

	@AfterClass
	public static void end() {
		Zats.cleanup();
	}

	@After
	public void after() {
		Zats.cleanup();
	}

	@Test
	public void testOpenAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/open.zul");

		Label target = desktop.query("#target").as(Label.class);
		Label event = desktop.query("#eventName").as(Label.class);
		Label flag = desktop.query("#flag").as(Label.class);
		assertEquals("", target.getValue());
		assertEquals("", event.getValue());
		assertEquals("", flag.getValue());

		String targetName = "bandbox";
		OpenAgent agent = desktop.query("#bandbox").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "combobox";
		agent = desktop.query("#combobox").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "groupbox";
		agent = desktop.query("#groupbox").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals("groupbox", target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "detail";
		agent = desktop.query("#detail").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "group";
		agent = desktop.query("#group").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "listgroup";
		agent = desktop.query("#listgroup").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "treeitem";
		agent = desktop.query("#treeitem1").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "treeitem";
		agent = desktop.query("#treeitem1-2").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "panel";
		agent = desktop.query("#pane").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());

		targetName = "window";
		agent = desktop.query("#win").as(OpenAgent.class);
		agent.open(true);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
		agent.open(false);
		assertEquals("onOpen", event.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("false", flag.getValue());
	}

	@Test
	public void testSelectAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/multiple-select.zul");

		Label msg = desktop.query("#msg").as(Label.class);
		assertEquals("", msg.getValue());

		// listbox 
		desktop.query("#lb1 #li1").as(SelectAgent.class).select();
		assertTrue(msg.getValue().contains("li1"));
		desktop.query("#lb1 #li2").as(MultipleSelectAgent.class).select();
		assertTrue(msg.getValue().contains("li1") && msg.getValue().contains("li2"));
		desktop.query("#lb1 #li1").as(MultipleSelectAgent.class).deselect();
		assertTrue(!msg.getValue().contains("li1") && msg.getValue().contains("li2"));

		// tree
		desktop.query("#t1 #ti1").as(SelectAgent.class).select();
		assertTrue(msg.getValue().contains("ti1"));
		desktop.query("#t1 #ti2").as(MultipleSelectAgent.class).select();
		assertTrue(msg.getValue().contains("ti1") && msg.getValue().contains("ti2"));
		desktop.query("#t1 #ti1").as(MultipleSelectAgent.class).deselect();
		assertTrue(!msg.getValue().contains("ti1") && msg.getValue().contains("ti2"));

		// tabbox
		desktop.query("#tb1 #tab1").as(SelectAgent.class).select();
		assertEquals("tab1", msg.getValue());
		desktop.query("#tb1 #tab2").as(SelectAgent.class).select();
		assertEquals("tab2", msg.getValue());
	}

	@Test
	public void testCheckAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/check.zul");

		Label msg = desktopAgent.query("#msg").as(Label.class);
		assertTrue(msg.getValue().length() <= 0);

		// test checkbox and menuitem
		String label = "";
		for (int i = 1; i <= 5; ++i) {
			ComponentAgent c = desktopAgent.query("#c" + i);
			c.as(CheckAgent.class).check(true);
			label += "c" + i + " ";
			assertEquals(label.trim(), msg.getValue().trim());
		}
		// test radiogroup
		for (int i = 7; i <= 9; ++i) {
			desktopAgent.query("#c" + i).as(CheckAgent.class).check(true);
			label += "c" + i + " ";
			assertEquals(label.trim(), msg.getValue().trim());
		}
	}

	@Test
	public void testClickAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/click.zul");

		Label msg = desktopAgent.query("#msg").as(Label.class);
		assertEquals("Hello World!", desktopAgent.query("#msg").as(Label.class).getValue());
		desktopAgent.query("#btn").as(ClickAgent.class).click();
		assertEquals("Welcome", desktopAgent.query("#msg").as(Label.class).getValue());
	}

	@Test
	public void testInputAgent1() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/type1.zul");

		Label l;
		ComponentAgent inp;

		//textbox
		l = desktopAgent.query("#l10").as(Label.class);
		inp = desktopAgent.query("#inp10");
		Assert.assertEquals("", l.getValue());
		inp.type("abc");
		Assert.assertEquals("abc", l.getValue());

		//intbox
		l = desktopAgent.query("#l7").as(Label.class);
		inp = desktopAgent.query("#inp7");
		Assert.assertEquals("", l.getValue());
		inp.type("123");
		Assert.assertEquals("123", l.getValue());

		//longbox
		l = desktopAgent.query("#l8").as(Label.class);
		inp = desktopAgent.query("#inp8");
		Assert.assertEquals("", l.getValue());
		inp.type("456");
		Assert.assertEquals("456", l.getValue());

		//decimalbox
		l = desktopAgent.query("#l4").as(Label.class);
		inp = desktopAgent.query("#inp4");
		Assert.assertEquals("", l.getValue());
		inp.type("1.0");
		Assert.assertEquals("1.0", l.getValue());

		//doublebox
		l = desktopAgent.query("#l5").as(Label.class);
		inp = desktopAgent.query("#inp5");
		Assert.assertEquals("", l.getValue());
		inp.type("2.0");
		Assert.assertEquals("2.0", l.getValue());

		//doublespinner
		l = desktopAgent.query("#l6").as(Label.class);
		inp = desktopAgent.query("#inp6");
		Assert.assertEquals("", l.getValue());
		inp.type("3.0");
		Assert.assertEquals("3.0", l.getValue());

		//datebox
		l = desktopAgent.query("#l3").as(Label.class);
		inp = desktopAgent.query("#inp3");
		Assert.assertEquals("", l.getValue());
		inp.type("20120320");
		Assert.assertEquals("20120320", l.getValue());

		//timebox
		l = desktopAgent.query("#l11").as(Label.class);
		inp = desktopAgent.query("#inp11");
		Assert.assertEquals("", l.getValue());
		inp.type("13:11");
		Assert.assertEquals("13:11", l.getValue());

		//spinner
		l = desktopAgent.query("#l9").as(Label.class);
		inp = desktopAgent.query("#inp9");
		Assert.assertEquals("", l.getValue());
		inp.type("4");
		Assert.assertEquals("4", l.getValue());

		//combobox
		l = desktopAgent.query("#l2").as(Label.class);
		inp = desktopAgent.query("#inp2");
		Assert.assertEquals("", l.getValue());
		inp.type("abc");
		Assert.assertEquals("abc", l.getValue());

		//bandbox
		l = desktopAgent.query("#l1").as(Label.class);
		inp = desktopAgent.query("#inp1");
		Assert.assertEquals("", l.getValue());
		inp.type("def");
		Assert.assertEquals("def", l.getValue());

		//colorbox
		l = desktopAgent.query("#l12").as(Label.class);
		inp = desktopAgent.query("colorbox");
		Assert.assertEquals("", l.getValue());
		inp.type("#0000ff");
		Assert.assertEquals("#0000ff", l.getValue());
	}

	@Test
	public void testInputAgent2() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/type1.zul");

		Label l;
		ComponentAgent inp;

		//decimalbox
		l = desktopAgent.query("#l4").as(Label.class);
		inp = desktopAgent.query("#inp4");
		Assert.assertEquals("", l.getValue());
		inp.type("1.0");
		Assert.assertEquals("1.0", l.getValue());
		inp.type("-1");
		Assert.assertEquals("1.0", l.getValue());
		inp.type("2,222.33");
		Assert.assertEquals("2222.33", l.getValue());

		//doublebox
		l = desktopAgent.query("#l5").as(Label.class);
		inp = desktopAgent.query("#inp5");
		Assert.assertEquals("", l.getValue());
		inp.type("2.0");
		Assert.assertEquals("2.0", l.getValue());
		inp.type("-2");
		Assert.assertEquals("2.0", l.getValue());
		inp.type("3,333.33");
		Assert.assertEquals("3333.33", l.getValue());

		//doublespinner
		l = desktopAgent.query("#l6").as(Label.class);
		inp = desktopAgent.query("#inp6");
		Assert.assertEquals("", l.getValue());
		inp.type("3.0");
		Assert.assertEquals("3.0", l.getValue());
		inp.type("-3");
		Assert.assertEquals("3.0", l.getValue());
		inp.type("4,444.33");
		Assert.assertEquals("4444.33", l.getValue());
	}

	@Test
	public void testInputAgent3() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/type2.zul");

		Label l;
		ComponentAgent inp;

		//decimalbox
		l = desktopAgent.query("#l4").as(Label.class);
		inp = desktopAgent.query("#inp4");
		Assert.assertEquals("", l.getValue());
		inp.type("1.0");
		Assert.assertEquals("1.0", l.getValue());
		inp.type("2,222.33");
		assertTrue(l.getValue().replace(",", "").equals("2222.33"));

		//doublebox
		l = desktopAgent.query("#l5").as(Label.class);
		inp = desktopAgent.query("#inp5");
		Assert.assertEquals("", l.getValue());
		inp.type("2.0");
		Assert.assertEquals("2.0", l.getValue());
		inp.type("3,333.33");
		assertTrue(l.getValue().replace(",", "").equals("3333.33"));

		//doublespinner
		l = desktopAgent.query("#l6").as(Label.class);
		inp = desktopAgent.query("#inp6");
		Assert.assertEquals("", l.getValue());
		inp.type("3.0");
		Assert.assertEquals("3.0", l.getValue());
		inp.type("4,444.33");
		assertTrue(l.getValue().replace(",", "").equals("4444.33"));
	}

	@Test
	public void testTypeAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/type1.zul");

		Label l;
		ComponentAgent inp;

		//textbox
		l = desktopAgent.query("#l10").as(Label.class);
		inp = desktopAgent.query("#inp10");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("abc");
		Assert.assertEquals("abc", l.getValue());

		//intbox
		l = desktopAgent.query("#l7").as(Label.class);
		inp = desktopAgent.query("#inp7");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("123");
		Assert.assertEquals("123", l.getValue());

		//longbox
		l = desktopAgent.query("#l8").as(Label.class);
		inp = desktopAgent.query("#inp8");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("456");
		Assert.assertEquals("456", l.getValue());

		//decimalbox
		l = desktopAgent.query("#l4").as(Label.class);
		inp = desktopAgent.query("#inp4");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("1.0");
		Assert.assertEquals("1.0", l.getValue());

		//doublebox
		l = desktopAgent.query("#l5").as(Label.class);
		inp = desktopAgent.query("#inp5");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("2.0");
		Assert.assertEquals("2.0", l.getValue());

		//doublespinner
		l = desktopAgent.query("#l6").as(Label.class);
		inp = desktopAgent.query("#inp6");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("3.0");
		Assert.assertEquals("3.0", l.getValue());

		//datebox
		l = desktopAgent.query("#l3").as(Label.class);
		inp = desktopAgent.query("#inp3");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("20120320");
		Assert.assertEquals("20120320", l.getValue());

		//timebox
		l = desktopAgent.query("#l11").as(Label.class);
		inp = desktopAgent.query("#inp11");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("13:11");
		Assert.assertEquals("13:11", l.getValue());

		//spinner
		l = desktopAgent.query("#l9").as(Label.class);
		inp = desktopAgent.query("#inp9");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("4");
		Assert.assertEquals("4", l.getValue());

		//combobox
		l = desktopAgent.query("#l2").as(Label.class);
		inp = desktopAgent.query("#inp2");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("abc");
		Assert.assertEquals("abc", l.getValue());

		//bandbox
		l = desktopAgent.query("#l1").as(Label.class);
		inp = desktopAgent.query("#inp1");
		Assert.assertEquals("", l.getValue());
		inp.as(InputAgent.class).type("def");
		Assert.assertEquals("def", l.getValue());
	}

	@Test
	public void testClickAll() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/click-all.zul");

		Label history = desktop.query("#history").as(Label.class);
		ComponentAgent comps = desktop.query("#comps");
		assertNotNull(comps);
		
		for (String name : componentNames) {
			List<ComponentAgent> cas = comps.queryAll(name);
			if (cas.isEmpty()) continue;
			for (ComponentAgent ca : cas) {
				String id = ca.getId();
				if (id == null || id.isEmpty() || "history".equals(id) || "menuitem".equals(id) || "menuseparator".equals(id) || "popup".equals(id) || "menubar".equals(id) || "menu".equals(id)) continue;
				ClickAgent agent = ca.as(ClickAgent.class);
				agent.click();
				assertTrue(id + " click not found in " + history.getValue(), history.getValue().contains("onClick:" + id + ";"));
				agent.doubleClick();
				assertTrue(id + " doubleClick not found in " + history.getValue(), history.getValue().contains("onDoubleClick:" + id + ";"));
				agent.rightClick();
				assertTrue(id + " rightClick not found in " + history.getValue(), history.getValue().contains("onRightClick:" + id + ";"));
			}
		}
	}

	@Test
	public void testHover() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/click-all.zul");

		Label history = desktop.query("#history").as(Label.class);
		ComponentAgent comps = desktop.query("#comps");
		assertNotNull(comps);
		
		for (String name : componentNames) {
			List<ComponentAgent> cas = comps.queryAll(name);
			if (cas.isEmpty()) continue;
			for (ComponentAgent ca : cas) {
				String id = ca.getId();
				if (id == null || id.isEmpty() || "history".equals(id) || "menuitem".equals(id) || "menuseparator".equals(id) || "popup".equals(id) || "menubar".equals(id) || "menu".equals(id)) continue;
				HoverAgent agent = ca.as(HoverAgent.class);
				agent.moveOver();
				assertTrue(id + " mouseOver not found in " + history.getValue(), history.getValue().contains("onMouseOver:" + id + ";"));
				agent.moveOut();
				assertTrue(id + " mouseOut not found in " + history.getValue(), history.getValue().contains("onMouseOut:" + id + ";"));
			}
		}
	}

	@Test
	public void testMaxMinAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/max-min.zul");

		Label eventName = desktop.query("#eventName").as(Label.class);
		Label target = desktop.query("#target").as(Label.class);
		Label flag = desktop.query("#flag").as(Label.class);

		String targetName = "window";
		SizeAgent agent = desktop.query("#win").as(SizeAgent.class);
		agent.maximize(true);
		assertEquals("onMaximize", eventName.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());

		agent.minimize(true);
		assertEquals("onMinimize", eventName.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());

		targetName = "panel";
		agent = desktop.query("#pane").as(SizeAgent.class);
		
		agent.maximize(true);
		assertEquals("onMaximize", eventName.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());

		agent.minimize(true);
		assertEquals("onMinimize", eventName.getValue());
		assertEquals(targetName, target.getValue());
		assertEquals("true", flag.getValue());
	}

	@Test
	public void testUploadAgent() throws Exception {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/upload.zul");
		Vbox results = desktop.query("#results").as(Vbox.class);
		assertEquals(0, results.getChildren().size());

		// prepare files for testing 
		File textFile = File.createTempFile("zats-upload-text-", ".tmp");
		textFile.deleteOnExit();
		String text = "Hello! World!\r\nHello! ZK!";
		byte[] textRaw = text.getBytes("utf-8");
		String binary = toHexString(textRaw).toUpperCase();
		FileOutputStream fos = new FileOutputStream(textFile);
		fos.write(textRaw);
		fos.close();

		// binary file
		for (int i = 0; i < 4; ++i) {
			desktop.query("#clean").as(ClickAgent.class).click();
			String id = "#btn" + i;
			UploadAgent agent = desktop.query(id).as(UploadAgent.class);
			agent.upload(textFile, null);
			agent.finish();
			Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
			Assert.assertEquals("application/octet-stream", desktop.query("#file0 .contentType").as(Label.class).getValue());
			Assert.assertEquals(ContentTypes.getFormat("application/octet-stream"), desktop.query("#file0 .format").as(Label.class).getValue());
			Assert.assertEquals(binary, desktop.query("#file0 .binary").as(Label.class).getValue());
			assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));
		}

		// content type
		desktop.query("#clean").as(ClickAgent.class).click();
		UploadAgent agent = desktop.query("#btn0").as(UploadAgent.class);
		agent.upload(textFile, "text/plain");
		agent.finish();
		Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
		Assert.assertEquals(binary, desktop.query("#file0 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));

		// text stream
		desktop.query("#clean").as(ClickAgent.class).click();
		agent = desktop.query("#btn0").as(UploadAgent.class);
		agent.upload("sample.txt", new ByteArrayInputStream(textRaw), "text/plain");
		agent.finish();
		Assert.assertEquals("sample.txt", desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
		Assert.assertEquals(binary, desktop.query("#file0 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));
	}

	@Test
	public void testUploadAgentWithDialog() throws Exception {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/upload.zul");
		Vbox results = desktop.query("#results").as(Vbox.class);
		assertEquals(0, results.getChildren().size());

		// prepare files for testing 
		File textFile = File.createTempFile("zats-upload-text-", ".tmp");
		textFile.deleteOnExit();
		String text = "Hello! World!\r\nHello! ZK!";
		byte[] textRaw = text.getBytes("utf-8");
		String textBinary = toHexString(textRaw).toUpperCase();
		FileOutputStream fos = new FileOutputStream(textFile);
		fos.write(textRaw);
		fos.close();

		File imageFile = File.createTempFile("zats-upload-image-", ".png");
		imageFile.deleteOnExit();
		byte[] imageRaw = new byte[] { (byte) -119, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10, (byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 73, (byte) 72, (byte) 68, (byte) 82, (byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 0, (byte) 0,
				(byte) 0, (byte) 10, (byte) 8, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 80, (byte) 88, (byte) -22, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 103, (byte) 65, (byte) 77, (byte) 65, (byte) 0, (byte) 0, (byte) -79, (byte) 11, (byte) 11, (byte) -3, (byte) 97, (byte) 5, (byte) 0, (byte) 0,
				(byte) 0, (byte) 9, (byte) 112, (byte) 72, (byte) 89, (byte) 115, (byte) 0, (byte) 0, (byte) 18, (byte) 116, (byte) 0, (byte) 10, (byte) 18, (byte) 116, (byte) 1, (byte) -3, (byte) 102, (byte) 31, (byte) 120, (byte) 0, (byte) 0, (byte) 0, (byte) 39, (byte) 73, (byte) 68, (byte) 65, (byte) 84,
				(byte) 40, (byte) 83, (byte) 99, (byte) 124, (byte) 43, (byte) -2, (byte) 83, (byte) -2, (byte) 128, (byte) 4, (byte) 76, (byte) -10, (byte) 32, (byte) 115, (byte) -104, (byte) -103, (byte) 57, (byte) -108, (byte) 74, (byte) 51, (byte) 42, (byte) 109, (byte) -32,
				(byte) 65, (byte) -34, (byte) 82, (byte) 118, (byte) 122, (byte) -2, (byte) 96, (byte) 113, (byte) 26, (byte) 0, (byte) -104, (byte) -2, (byte) 4, (byte) -10, (byte) -3, (byte) -10, (byte) 83, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 73, (byte) 69, (byte) 78, (byte) 68,
				(byte) -82, (byte) 66, (byte) 96, (byte) -126 };
		String imageBinary = toHexString(imageRaw).toUpperCase();
		fos = new FileOutputStream(imageFile);
		fos.write(imageRaw);
		fos.close();

		// text
		desktop.query("#clean").as(ClickAgent.class).click();
		desktop.query("#btn4").as(ClickAgent.class).click();
		UploadAgent agent = desktop.as(UploadAgent.class);
		agent.upload(textFile, "text/plain");
		agent.finish();
		Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));

		// binary
		desktop.query("#clean").as(ClickAgent.class).click();
		desktop.query("#btn4").as(ClickAgent.class).click();
		agent = desktop.as(UploadAgent.class);
		agent.upload(textFile, "application/octet-stream");
		agent.finish();
		Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("application/octet-stream", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals(ContentTypes.getFormat("application/octet-stream"), desktop.query("#file0 .format").as(Label.class).getValue());
		Assert.assertEquals(textBinary, desktop.query("#file0 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));

		// image
		desktop.query("#clean").as(ClickAgent.class).click();
		desktop.query("#btn4").as(ClickAgent.class).click();
		agent = desktop.as(UploadAgent.class);
		agent.upload(imageFile, "image/png");
		agent.finish();
		Assert.assertEquals(imageFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("image/png", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("png", desktop.query("#file0 .format").as(Label.class).getValue());
		Assert.assertEquals(imageBinary, desktop.query("#file0 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .width").as(Label.class).getValue().startsWith("10") || desktop.query("#file0 .width").as(Label.class).getValue().equals("-1px"));
		assertTrue(desktop.query("#file0 .height").as(Label.class).getValue().startsWith("10") || desktop.query("#file0 .height").as(Label.class).getValue().equals("-1px"));

		// multiple
		desktop.query("#clean").as(ClickAgent.class).click();
		desktop.query("#btn5").as(ClickAgent.class).click();
		agent = desktop.as(UploadAgent.class);
		agent.upload(textFile, "text/plain");
		agent.upload(textFile, "application/octet-stream");
		agent.upload(imageFile, "image/png");
		agent.finish();
		Assert.assertEquals("3", desktop.query("#resultsCount").as(Label.class).getValue()); // results count
		// text
		Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
		assertTrue(desktop.query("#file0 .text").as(Label.class).getValue().trim().contains("Hello! World!"));
		// binary
		Assert.assertEquals(textFile.getName(), desktop.query("#file1 .name").as(Label.class).getValue());
		Assert.assertEquals("application/octet-stream", desktop.query("#file1 .contentType").as(Label.class).getValue());
		Assert.assertEquals(ContentTypes.getFormat("application/octet-stream"), desktop.query("#file1 .format").as(Label.class).getValue());
		Assert.assertEquals(textBinary, desktop.query("#file1 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file1 .text").as(Label.class).getValue().trim().contains("Hello! World!"));
		// image
		Assert.assertEquals(imageFile.getName(), desktop.query("#file2 .name").as(Label.class).getValue());
		Assert.assertEquals("image/png", desktop.query("#file2 .contentType").as(Label.class).getValue());
		Assert.assertEquals("png", desktop.query("#file2 .format").as(Label.class).getValue());
		Assert.assertEquals(imageBinary, desktop.query("#file2 .binary").as(Label.class).getValue());
		assertTrue(desktop.query("#file2 .width").as(Label.class).getValue().startsWith("10") || desktop.query("#file2 .width").as(Label.class).getValue().equals("-1px"));
		assertTrue(desktop.query("#file2 .height").as(Label.class).getValue().startsWith("10") || desktop.query("#file2 .height").as(Label.class).getValue().equals("-1px"));
	}

	@Test
	public void testRendererAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/render.zul");
		desktop.query("#init").as(ClickAgent.class).click();

		Label ic = desktop.query("#listitemContent").as(Label.class);
		Label rc = desktop.query("#rowContent").as(Label.class);
		ComponentAgent index = desktop.query("#index");

		// test non-loaded items
		for (int i = 900; i <= 910; ++i) {
			index.type("" + i);
		}

		// render items
		desktop.query("#listbox").as(RenderAgent.class).render(900, 949);
		desktop.query("#grid").as(RenderAgent.class).render(900, 949);

		for (int i = 900; i <= 949; ++i) {
			index.type("" + i);
			assertTrue(ic.getValue().contains("item" + i));
			assertTrue(rc.getValue().contains("item" + i));
		}
	}
}
