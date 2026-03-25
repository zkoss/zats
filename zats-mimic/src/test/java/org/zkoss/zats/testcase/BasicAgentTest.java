/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.zkoss.zats.mimic.impl.operation.SwitchedSortAgentImpl;
import org.zkoss.zats.mimic.operation.AuAgent;
import org.zkoss.zats.mimic.operation.AuData;
import org.zkoss.zats.mimic.operation.BookmarkAgent;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.CloseAgent;
import org.zkoss.zats.mimic.operation.DragAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;
import org.zkoss.zats.mimic.operation.GroupAgent;
import org.zkoss.zats.mimic.operation.HoverAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zats.mimic.operation.KeyStrokeAgent;
import org.zkoss.zats.mimic.operation.MoveAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zats.mimic.operation.PagingAgent;
import org.zkoss.zats.mimic.operation.RenderAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.SizeAgent;
import org.zkoss.zats.mimic.operation.SortAgent;
import org.zkoss.zats.mimic.operation.UploadAgent;
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
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;

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

	private static final String[] componentNames = { "a", "button", "captcha", "fileupload", "html",
			"include", "image", "imagemap", "label", "menubar", "menu", "menuitem", "menuseparator",
			"popup", "progressmeter", "separator", "space", "toolbar", "toolbarbutton", "bandbox", "colorbox",
			"combobox", "comboitem", "datebox", "decimalbox", "doublebox", "doublespinner", "intbox", "longbox",
			"spinner", "textbox", "timebox", "checkbox", "radio", "radiogroup", "slider", "caption", "div",
			"groupbox", "panel", "span", "tabbox", "tab", "window", "grid", "detail", "group", "listbox",
			"listitem", "listgroup", "tree", "treeitem" };

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

	@Test
	public void testKeyStrokeAgent(){
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/keystroke.zul");

		ComponentAgent inp1 = desktopAgent.query("#inp1");
		ComponentAgent inp2 = desktopAgent.query("#inp2");
		ComponentAgent l1 = desktopAgent.query("#l1");

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
	public void testOpenAgentTree(){
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/open-tree.zul");

		ComponentAgent tree = desktopAgent.query("#tree");
		List<ComponentAgent> items = tree.queryAll("treeitem");
		Assert.assertEquals(2, items.size());

		Stack<ComponentAgent> stack = new Stack<ComponentAgent>();
		stack.addAll(items);

		while(!stack.empty()){
			ComponentAgent item = stack.pop();

			if(item.query("treechildren")!=null){
				Assert.assertFalse(item.as(Treeitem.class).isOpen());
				items = item.query("treechildren").queryAll("treeitem");//the sub-treeitem.
				Assert.assertEquals(0, items.size());

				item.as(OpenAgent.class).open(true);//trigger open to load the tree item.

				Assert.assertTrue(item.as(Treeitem.class).isOpen());
				items = item.query("treechildren").queryAll("treeitem");//the sub-treeitem.
				Assert.assertEquals(2, items.size());
				for(ComponentAgent si:items){
					stack.push(si);
				}
			}
		}

		items = tree.queryAll("treeitem");
		Assert.assertEquals(14, items.size());
	}

	@Test
	public void testFocusAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/focus.zul");
		Label curr = desktopAgent.query("#current").as(Label.class);
		Label lost = desktopAgent.query("#lost").as(Label.class);
		assertTrue(curr.getValue().length() <= 0);
		assertTrue(curr.getValue().length() <= 0);

		for (int i = 1; i <= 17; ++i) {
			ComponentAgent comp = desktopAgent.query("#c" + i);
			comp.as(FocusAgent.class).focus();
			String name = comp.as(AbstractComponent.class).getDefinition().getName();
			assertEquals(name, curr.getValue());
			comp.as(FocusAgent.class).blur();
			assertEquals(name, lost.getValue());
		}
	}

	@Test
	public void testMultipleSelectAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/multiple-select-legacy.zul");

		Label msg = desktopAgent.query("#msg").as(Label.class);
		assertEquals("", msg.getValue());

		ComponentAgent listbox = desktopAgent.query("#lb");
		assertEquals(4, listbox.as(Listbox.class).getChildren().size()); // include header
		List<ComponentAgent> items = listbox.queryAll("listitem");

		// listbox multiple selection
		items.get(0).as(MultipleSelectAgent.class).select();
		assertEquals("[i0]", msg.getValue());
		assertEquals(1, listbox.as(Listbox.class).getSelectedCount());
		items.get(1).as(MultipleSelectAgent.class).select();
		assertEquals("[i0, i1]", msg.getValue());
		assertEquals(2, listbox.as(Listbox.class).getSelectedCount());
		items.get(2).as(MultipleSelectAgent.class).select();
		assertEquals("[i0, i1, i2]", msg.getValue());
		assertEquals(3, listbox.as(Listbox.class).getSelectedCount());
		items.get(1).as(MultipleSelectAgent.class).deselect();
		assertEquals("[i0, i2]", msg.getValue());
		assertEquals(2, listbox.as(Listbox.class).getSelectedCount());
		items.get(0).as(MultipleSelectAgent.class).deselect();
		assertEquals("[i2]", msg.getValue());
		assertEquals(1, listbox.as(Listbox.class).getSelectedCount());
		items.get(2).as(MultipleSelectAgent.class).deselect();
		assertEquals("[]", msg.getValue());
		assertEquals(0, listbox.as(Listbox.class).getSelectedCount());
		items.get(2).as(MultipleSelectAgent.class).deselect(); // should happen nothing
		assertEquals("[]", msg.getValue());
		assertEquals(0, listbox.as(Listbox.class).getSelectedCount());

		// listbox single selection (extra test)
		desktopAgent.query("#lbcb checkbox").as(CheckAgent.class).check(false);
		String[] values = { "[i0]", "[i1]", "[i2]" };
		for (int i = 0; i < 3; ++i) {
			items.get(i).as(SelectAgent.class).select();
			assertEquals(values[i], msg.getValue());
		}

		// tree multiple selection
		desktopAgent.query("#ti1").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-1").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1, ti1-1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-1").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1, ti1-1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1-1").as(MultipleSelectAgent.class).deselect();
		assertEquals("[]", msg.getValue());

		// tree multiple selection - single select at multiple selection mode
		desktopAgent.query("#ti1-2").as(SelectAgent.class).select();
		assertEquals("[ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-1").as(SelectAgent.class).select();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1").as(SelectAgent.class).select();
		assertEquals("[ti1]", msg.getValue());

		// tree multiple selection - with check mark
		desktopAgent.queryAll("#tcb > checkbox").get(1).as(CheckAgent.class).check(true);
		assertTrue(desktopAgent.query("#t").as(Tree.class).isCheckmark());

		desktopAgent.query("#ti1").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-1").as(MultipleSelectAgent.class).select();
		assertEquals("[ti1, ti1-1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1, ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1-2").as(MultipleSelectAgent.class).deselect();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1-1").as(MultipleSelectAgent.class).deselect();
		assertEquals("[]", msg.getValue());

		// tree single selection (extra test)
		desktopAgent.queryAll("#tcb > checkbox").get(0).as(CheckAgent.class).check(false);
		assertFalse(desktopAgent.query("#t").as(Tree.class).isMultiple());

		desktopAgent.query("#ti1-2").as(SelectAgent.class).select();
		assertEquals("[ti1-2]", msg.getValue());
		desktopAgent.query("#ti1-1").as(SelectAgent.class).select();
		assertEquals("[ti1-1]", msg.getValue());
		desktopAgent.query("#ti1").as(SelectAgent.class).select();
		assertEquals("[ti1]", msg.getValue());

		try {
			desktopAgent.query("#ti1").as(MultipleSelectAgent.class).select();
			fail();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCloseAgent(){
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/close.zul");

		ComponentAgent panel = desktopAgent.query("panel[title='closable']");
		panel.as(CloseAgent.class).close();
		Assert.assertNull(((Component)panel.getDelegatee()).getPage());

		ComponentAgent window = desktopAgent.query("window[title='closable']");
		window.as(CloseAgent.class).close();
		Assert.assertNull(((Component)window.getDelegatee()).getPage());

		ComponentAgent tab = desktopAgent.query("tab[label='closable']");
		tab.as(CloseAgent.class).close();
		Assert.assertNull(((Component)tab.getDelegatee()).getPage());

		// TODO close a closable=false component, it will still be closed. ignore this case for now.
		//	panel = desktopAgent.query("panel[title='non-close']");
		//	panel.as(CloseAgent.class).close();
		//	Assert.assertNotNull(panel.getDelegatee().getPage());
	}

	@Test
	public void testCKEditorInputAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/type-ckeditor.zul");

		Label eventName = desktop.query("#eventName").as(Label.class);
		Label change = desktop.query("#change").as(Label.class);
		Label changing = desktop.query("#changing").as(Label.class);
		assertEquals("", eventName.getValue());
		assertEquals("", change.getValue());
		assertEquals("", changing.getValue());

		desktop.query("#ck").as(InputAgent.class).typing("Hello");
		assertEquals("onChanging", eventName.getValue());
		assertEquals("", change.getValue());
		assertEquals("Hello", changing.getValue());

		desktop.query("#ck").as(InputAgent.class).type("Hello world");
		assertEquals("onChange", eventName.getValue());
		assertEquals("Hello world", change.getValue());
		assertEquals("Hello", changing.getValue());
	}

	@Test
	public void testKeyStrokeAgentOnInputElements() {
		// prepare all CtrlKey strings
		char[] words = new char[26];
		for (char c = 'a'; c <= 'z'; ++c)
			words[(int) (c - 'a')] = c;

		char[] numbers = new char[10];
		for (char c = '0'; c <= '9'; ++c)
			numbers[(int) (c - '0')] = c;

		String[] keys = { "#home", "#end", "#ins", "#del", "#bak", "#left", "#right", "#up", "#down", "#pgup", "#pgdn",
				"#f1", "#f2", "#f3", "#f4", "#f5", "#f6", "#f7", "#f8", "#f9", "#f10", "#f11", "#f12" };
		//javascript key code, http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
		int[] keycodes = { 36, 35, 45, 46, 8, 37, 39, 38, 40, 33, 34, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122,
				123 };

		Map<String, String> map = new HashMap<String, String>();
		map.put(keys[0], "" + keycodes[0]);
		map.put(keys[1], "" + keycodes[1]);

		List<String> ctrls = new ArrayList<String>();
		List<String> alts = new ArrayList<String>();
		List<String> shifts = new ArrayList<String>();
		int c = 65;
		for (char w : words) {
			ctrls.add("^" + w);
			alts.add("@" + w);
			map.put("^" + w, "" + c);
			map.put("@" + w, "" + c);
			c++;
		}
		c = 48;
		for (char n : numbers) {
			ctrls.add("^" + n);
			alts.add("@" + n);
			map.put("^" + n, "" + c);
			map.put("@" + n, "" + c);
			c++;
		}
		for (int i = 0; i < keys.length; ++i) {
			String n = keys[i];
			ctrls.add("^" + n);
			alts.add("@" + n);
			shifts.add("$" + n);
			map.put("^" + n, "" + keycodes[i]);
			map.put("@" + n, "" + keycodes[i]);
			map.put("$" + n, "" + keycodes[i]);
		}

		DesktopAgent desktop = Zats.newClient().connect("/~./basic/keystroke-input.zul");

		Label target = desktop.query("#target").as(Label.class);
		Label ref = desktop.query("#ref").as(Label.class);
		Label event = desktop.query("#eventName").as(Label.class);
		Label code = desktop.query("#code").as(Label.class);
		Label ctrl = desktop.query("#ctrl").as(Label.class);
		assertEquals("", target.getValue());
		assertEquals("", ref.getValue());
		assertEquals("", event.getValue());
		assertEquals("", code.getValue());
		assertEquals("", ctrl.getValue());

		// components handle event
		List<ComponentAgent> comps = desktop.query("#bySelf").getChildren();
		assertEquals(17, comps.size());

		// onOk
		for (ComponentAgent comp : comps) {
			comp.stroke("#enter");
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), target.getValue());
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
			assertEquals(Events.ON_OK, event.getValue());
			assertEquals("13", code.getValue());
			assertEquals("none", ctrl.getValue());
		}

		// onCancel
		for (ComponentAgent comp : comps) {
			comp.stroke("#esc");
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), target.getValue());
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
			assertEquals(Events.ON_CANCEL, event.getValue());
			assertEquals("27", code.getValue());
			assertEquals("none", ctrl.getValue());
		}

		// onCtrlKey - ctrl
		for (String k : ctrls) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("ctrl", ctrl.getValue());
			}
		}

		// onCtrlKey - alt
		for (String k : alts) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("alt", ctrl.getValue());
			}
		}

		// onCtrlKey - shift
		for (String k : shifts) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("shift", ctrl.getValue());
			}
		}

		// parent component handle event
		ComponentAgent parent = desktop.query("#byParent");
		String targetName = ((Component)parent.getDelegatee()).getDefinition().getName();
		comps = parent.getChildren();
		assertEquals(17, comps.size());

		// onOk
		for (ComponentAgent comp : comps) {
			comp.stroke("#enter");
			assertEquals(targetName, target.getValue());
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
			assertEquals(Events.ON_OK, event.getValue());
			assertEquals("13", code.getValue());
			assertEquals("none", ctrl.getValue());
		}

		// onCancel
		for (ComponentAgent comp : comps) {
			comp.stroke("#esc");
			assertEquals(targetName, target.getValue());
			assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
			assertEquals(Events.ON_CANCEL, event.getValue());
			assertEquals("27", code.getValue());
			assertEquals("none", ctrl.getValue());
		}

		// onCtrlKey - ctrl
		for (String k : ctrls) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(targetName, target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("ctrl", ctrl.getValue());
			}
		}

		// onCtrlKey - alt
		for (String k : alts) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(targetName, target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("alt", ctrl.getValue());
			}
		}

		// onCtrlKey - shift
		for (String k : shifts) {
			for (ComponentAgent comp : comps) {
				comp.stroke(k);
				assertEquals(targetName, target.getValue());
				assertEquals(((Component)comp.getDelegatee()).getDefinition().getName(), ref.getValue());
				assertEquals(Events.ON_CTRL_KEY, event.getValue());
				assertEquals(map.get(k), code.getValue());
				assertEquals("shift", ctrl.getValue());
			}
		}
	}

	@Test
	public void testTypingAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/typing.zul");

		// labels for validation
		Label event = desktop.query("#eventName").as(Label.class);
		Label target = desktop.query("#target").as(Label.class);
		Label value = desktop.query("#value").as(Label.class);
		assertEquals("", event.getValue());
		assertEquals("", target.getValue());
		assertEquals("", value.getValue());

		// components handle event
		List<ComponentAgent> comps = desktop.query("#inputs").getChildren();
		assertEquals(11, comps.size());

		for (int i = 0; i < comps.size(); ++i) {
			// typing
			String text = "type " + i;
			ComponentAgent comp = comps.get(i);
			comp.as(InputAgent.class).typing(text);
			// validate
			assertEquals("onChanging", event.getValue());
			assertEquals(((Component) comp.getDelegatee()).getDefinition().getName(), target.getValue());
			assertEquals(text, value.getValue());
		}
	}

	@Test
	public void testDragDrop(){
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/drag.zul");
		ComponentAgent leftBox = desktop.query("#left");
		Assert.assertEquals(6, leftBox.queryAll("listitem").size());

		ComponentAgent rightBox = desktop.query("#right");
		Assert.assertEquals(2, rightBox.queryAll("listitem").size());
		Assert.assertNull(rightBox.query("listcell[label='ZK Forge']"));

		//move 1 item from left to right
		ComponentAgent draggedItem = leftBox.query("listcell[label='ZK Forge']").getParent();
		draggedItem.as(DragAgent.class).dropOn(rightBox);
		Assert.assertEquals(5, leftBox.queryAll("listitem").size());
		Assert.assertEquals(3, rightBox.queryAll("listitem").size());
		Assert.assertNotNull(rightBox.query("listcell[label='ZK Forge']"));

		//move lower item before upper item
		ComponentAgent upperItem = rightBox.query("listcell[label='ZK Studio']").getParent();
		ComponentAgent lowerItem = rightBox.query("listcell[label='ZK Forge']").getParent();
		Assert.assertEquals(1, upperItem.as(Listitem.class).getIndex());
		Assert.assertEquals(2, lowerItem.as(Listitem.class).getIndex());
		lowerItem.as(DragAgent.class).dropOn(upperItem);
		Assert.assertEquals(2, upperItem.as(Listitem.class).getIndex());
		Assert.assertEquals(1, lowerItem.as(Listitem.class).getIndex());
	}

	@Test
	public void testSizeOperation() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/size.zul");
		Label eventName = desktop.query("#eventName").as(Label.class);
		Label target = desktop.query("#target").as(Label.class);
		Label width = desktop.query("#width").as(Label.class);
		Label height = desktop.query("#height").as(Label.class);
		assertEquals("", eventName.getValue());
		assertEquals("", target.getValue());
		assertEquals("", width.getValue());
		assertEquals("", height.getValue());

		String targetName = "window";
		SizeAgent agent = desktop.query(targetName).as(SizeAgent.class);

		agent.resize(-1, -1); // do nothing
		assertEquals("", eventName.getValue());
		assertEquals("", target.getValue());
		assertEquals("", width.getValue());
		assertEquals("", height.getValue());

		int[][] args = {
			{ 50, -1 },
			{ -1, 50 },
			{ 100, 100 },
			{ -1, -1},
		};
		String[][] except = {
			{ "50px", "100px" }, // default min-height
			{ "50px", "50px" },
			{ "100px", "100px" },
			{ "100px", "100px" }, // do nothing
		};

		for (int i = 0; i < args.length; ++i) {
			agent.resize(args[i][0], args[i][1]);
			assertEquals("onSize", eventName.getValue());
			assertEquals(targetName, target.getValue());
			assertEquals(except[i][0], width.getValue());
			assertEquals(except[i][1], height.getValue());
		}

		targetName = "panel";
		agent = desktop.query(targetName).as(SizeAgent.class);

		args = new int[][]{
			{ -1, 50 },
			{ 50, -1 },
			{ 100, 100 },
			{ -1, -1},
		};
		except = new String[][]{
			{ "200px", "50px" }, // default min-width
			{ "50px", "50px" },
			{ "100px", "100px" },
			{ "100px", "100px" }, // do nothing
		};

		for (int i = 0; i < args.length; ++i) {
			agent.resize(args[i][0], args[i][1]);
			assertEquals("onSize", eventName.getValue());
			assertEquals(targetName, target.getValue());
			assertEquals(except[i][0], width.getValue());
			assertEquals(except[i][1], height.getValue());
		}
	}

	@Test
	public void testPaging(){
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/paging.zul");

		//listbox's paging
		ComponentAgent paging = desktop.query("listbox > paging");
		Assert.assertEquals(0, paging.as(Paging.class).getActivePage());

		paging.as(PagingAgent.class).moveTo(1);
		Assert.assertEquals("1", desktop.query("#listboxPageIndex").as(Label.class).getValue());

		//grid's paging
		paging = desktop.query("#grid > paging");
		Assert.assertEquals(0, paging.as(Paging.class).getActivePage());

		paging.as(PagingAgent.class).moveTo(1);
		Assert.assertEquals("1", desktop.query("#gridPageIndex").as(Label.class).getValue());

		//tree's paging
		paging = desktop.query("tree > paging");
		Assert.assertEquals(0, paging.as(Paging.class).getActivePage());

		paging.as(PagingAgent.class).moveTo(1);
		Assert.assertEquals("1", desktop.query("#treePageIndex").as(Label.class).getValue());

		//paging itself
		paging = desktop.query("#pg");
		paging.as(PagingAgent.class).moveTo(1);
		Assert.assertEquals("1", desktop.query("#leftGridPageIndex").as(Label.class).getValue());
		Assert.assertEquals("1", desktop.query("#rightGridPageIndex").as(Label.class).getValue());

		//move out of page bound
		try{
			paging.as(PagingAgent.class).moveTo(-1);
			fail();
		}catch(AgentException e){
			logger.fine("expected exception: "+e.getMessage());
		}
		try{
			paging.as(PagingAgent.class).moveTo(paging.as(Paging.class).getPageCount());
			fail();
		}catch(AgentException e){
			logger.fine("expected exception: "+e.getMessage());
		}
	}

	@Test
	public void testBookmarkAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/bookmark.zul");
		assertEquals("Hello World!", desktopAgent.query("#msg").as(Label.class).getValue());

		desktopAgent.as(BookmarkAgent.class).change("ABCD");
		assertEquals("Welcome ABCD", desktopAgent.query("#msg").as(Label.class).getValue());

		desktopAgent.query("#btn").as(ClickAgent.class).click();
		assertEquals("XYZ", desktopAgent.as(Desktop.class).getBookmark());
	}

	//	unsupported temporarily, the behavior of it isn't compatible with the resize agent.
	//	@Test
	public void testColumnSizeOperation() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/size-column.zul");
		Label eventName = desktop.query("#eventName").as(Label.class);
		Label target = desktop.query("#target").as(Label.class);
		Label index = desktop.query("#index").as(Label.class);
		Label width = desktop.query("#width").as(Label.class);
		Label previousWidth = desktop.query("#previousWidth").as(Label.class);
		assertEquals("", eventName.getValue());
		assertEquals("", target.getValue());
		assertEquals("", index.getValue());
		assertEquals("", width.getValue());
		assertEquals("", previousWidth.getValue());

		String[][] args = new String[][] {
				{"#gc0" , "-1" , "999"},
				{"#gc0" , "100" , "-1"},
				{"#gc0" , "110" , "-1"},
				{"#gc2" , "120" , "-1"},
				{"#gc1" , "130" , "-1"},
				{"#gc1" , "130" , "-1"},
		};
		String[][] except = new String[][] {
				{ "", "", "", "", "" },
				{ "onColSize", "columns", "0", "100px", "null" },
				{ "onColSize", "columns", "0", "110px", "100px" },
				{ "onColSize", "columns", "2", "120px", "200px" },
				{ "onColSize", "columns", "1", "130px", "200px" },
				{ "onColSize", "columns", "1", "130px", "200px" },
		};
		for (int i = 0; i < args.length; ++i) {
			String id = args[i][0];
			int w = Integer.parseInt(args[i][1]), h = Integer.parseInt(args[i][2]);
			desktop.query(id).as(SizeAgent.class).resize(w, h);
			assertEquals(except[i][0], eventName.getValue());
			assertEquals(except[i][1], target.getValue());
			assertEquals(except[i][2], index.getValue());
			assertEquals(except[i][3], width.getValue());
			assertEquals(except[i][4], previousWidth.getValue());
		}

		args = new String[][] {
				{"#lh0" , "-1" , "999"},
				{"#lh0" , "100" , "-1"},
				{"#lh0" , "110" , "-1"},
				{"#lh2" , "120" , "-1"},
				{"#lh1" , "130" , "-1"},
				{"#lh1" , "130" , "-1"},
		};
		except = new String[][] {
				{ "onColSize", "columns", "1", "130px", "200px" },
				{ "onColSize", "listhead", "0", "100px", "null" },
				{ "onColSize", "listhead", "0", "110px", "100px" },
				{ "onColSize", "listhead", "2", "120px", "200px" },
				{ "onColSize", "listhead", "1", "130px", "200px" },
				{ "onColSize", "listhead", "1", "130px", "200px" },
		};
		for (int i = 0; i < args.length; ++i) {
			String id = args[i][0];
			int w = Integer.parseInt(args[i][1]), h = Integer.parseInt(args[i][2]);
			desktop.query(id).as(SizeAgent.class).resize(w, h);
			assertEquals(except[i][0], eventName.getValue());
			assertEquals(except[i][1], target.getValue());
			assertEquals(except[i][2], index.getValue());
			assertEquals(except[i][3], width.getValue());
			assertEquals(except[i][4], previousWidth.getValue());
		}

		args = new String[][] {
				{"#tc0" , "-1" , "999"},
				{"#tc0" , "100" , "-1"},
				{"#tc0" , "110" , "-1"},
				{"#tc2" , "120" , "-1"},
				{"#tc1" , "130" , "-1"},
				{"#tc1" , "130" , "-1"},
		};
		except = new String[][] {
				{ "onColSize", "listhead", "1", "130px", "200px" },
				{ "onColSize", "treecols", "0", "100px", "null" },
				{ "onColSize", "treecols", "0", "110px", "100px" },
				{ "onColSize", "treecols", "2", "120px", "200px" },
				{ "onColSize", "treecols", "1", "130px", "200px" },
				{ "onColSize", "treecols", "1", "130px", "200px" },
		};
		for (int i = 0; i < args.length; ++i) {
			String id = args[i][0];
			int w = Integer.parseInt(args[i][1]), h = Integer.parseInt(args[i][2]);
			desktop.query(id).as(SizeAgent.class).resize(w, h);
			assertEquals(except[i][0], eventName.getValue());
			assertEquals(except[i][1], target.getValue());
			assertEquals(except[i][2], index.getValue());
			assertEquals(except[i][3], width.getValue());
			assertEquals(except[i][4], previousWidth.getValue());
		}
	}

	//column's label in group.zul
	final private String COLUMN_AUTHOR = "Author";
	final private String COLUMN_TITLE = "Title";

	@Test
	public void testGroup(){

		DesktopAgent desktop = Zats.newClient().connect("/~./basic/group-sort.zul");
		ComponentAgent groupingColumn = desktop.query("column[label='"+COLUMN_AUTHOR+"']");
		groupingColumn.as(GroupAgent.class).group();

		Label groupingLabel = desktop.query("#groupingColumn").as(Label.class);
		Assert.assertEquals(COLUMN_AUTHOR, groupingLabel.getValue());

		groupingColumn = desktop.query("column[label='"+COLUMN_TITLE+"']");
		groupingColumn.as(GroupAgent.class).group();

		Assert.assertEquals(COLUMN_TITLE, groupingLabel.getValue());
	}


	@Test
	public void testScroll() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/scroll.zul");
		Label msg1 = desktop.query("#msg1").as(Label.class);
		Assert.assertEquals("", msg1.getValue());

		// slider 1; 0 to 100
		Integer[] args = new Integer[] { 0, 50, 100 };
		String[] expected = new String[] {
				"s1,onScroll,0",
				"s1,onScroll,50",
				"s1,onScroll,100",
		};
		InputAgent slider = desktop.query("#s1").as(InputAgent.class);
		for (int i = 0; i < args.length; ++i) {
			slider.input(args[i]);
			assertEquals(expected[i], msg1.getValue());
		}

		// slider 2; 0 to 200
		args = new Integer[] { 0, 199 , 200 };
		expected = new String[] {
				"s2,onScroll,0",
				"s2,onScroll,199",
				"s2,onScroll,200",
		};
		slider = desktop.query("#s2").as(InputAgent.class);
		for (int i = 0; i < args.length; ++i) {
			slider.input(args[i]);
			assertEquals(expected[i], msg1.getValue());
		}

		// compatibility
		slider.input(1);
		assertEquals("s2,onScroll,1", msg1.getValue());
		slider.input(2L);
		assertEquals("s2,onScroll,2", msg1.getValue());
		slider.input((short) 3);
		assertEquals("s2,onScroll,3", msg1.getValue());
		slider.input((byte) 4);
		assertEquals("s2,onScroll,4", msg1.getValue());
		slider.input(BigInteger.valueOf(5L));
		assertEquals("s2,onScroll,5", msg1.getValue());
		slider.input("6");
		assertEquals("s2,onScroll,6", msg1.getValue());
		slider.input("   7   ");
		assertEquals("s2,onScroll,7", msg1.getValue());

		// out of bounds
		slider.input(200);
		assertEquals("s2,onScroll,200", msg1.getValue());
		try {
			slider.input(-1);
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input(201);
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}

		// wrong value, type or syntax
		try {
			slider.input(null);
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input("");
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input("   ");
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input("100px");
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input(100.0);
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
		try {
			slider.input("100.0");
			fail();
		} catch (AgentException e) {
			assertEquals("s2,onScroll,200", msg1.getValue());
		}
	}


	@Test
	public void testMoveAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/move.zul");
		Label target = desktop.query("#target").as(Label.class);
		Label eventName = desktop.query("#eventName").as(Label.class);
		Label left = desktop.query("#left").as(Label.class);
		Label top = desktop.query("#top").as(Label.class);
		Assert.assertEquals("", target.getValue());
		Assert.assertEquals("", eventName.getValue());
		Assert.assertEquals("", left.getValue());
		Assert.assertEquals("", top.getValue());

		String name = "window";
		int[][] args = new int[][]{
				{-100, -100},
				{-100, 100},
				{100, -100},
				{100, 100},
		};
		String[][] expected = new String[][]{
				{name , "onMove" , "-100px" , "-100px"},
				{name , "onMove" , "-100px" , "100px"},
				{name , "onMove" , "100px" , "-100px"},
				{name , "onMove" , "100px" , "100px"},
		};
		ComponentAgent comp = desktop.query(name);
		MoveAgent moveAgent = comp.as(MoveAgent.class);
		for (int i = 0; i < args.length; ++i) {
			moveAgent.moveTo(args[i][0], args[i][1]);
			Assert.assertEquals(expected[i][0], target.getValue());
			Assert.assertEquals(expected[i][1], eventName.getValue());
			Assert.assertEquals(expected[i][2], left.getValue());
			Assert.assertEquals(expected[i][3], top.getValue());
			Assert.assertEquals(left.getValue(), comp.as(HtmlBasedComponent.class).getLeft());
			Assert.assertEquals(top.getValue(), comp.as(HtmlBasedComponent.class).getTop());
		}

		name = "panel";
		args = new int[][]{
				{-100, -100},
				{-100, 100},
				{100, -100},
				{100, 100},
		};
		expected = new String[][]{
				{name , "onMove" , "-100px" , "-100px"},
				{name , "onMove" , "-100px" , "100px"},
				{name , "onMove" , "100px" , "-100px"},
				{name , "onMove" , "100px" , "100px"},
		};
		comp = desktop.query(name);
		moveAgent = comp.as(MoveAgent.class);
		for (int i = 0; i < args.length; ++i) {
			moveAgent.moveTo(args[i][0], args[i][1]);
			Assert.assertEquals(expected[i][0], target.getValue());
			Assert.assertEquals(expected[i][1], eventName.getValue());
			Assert.assertEquals(expected[i][2], left.getValue());
			Assert.assertEquals(expected[i][3], top.getValue());
			Assert.assertEquals(left.getValue(), comp.as(HtmlBasedComponent.class).getLeft());
			Assert.assertEquals(top.getValue(), comp.as(HtmlBasedComponent.class).getTop());
		}
	}

		/*
		 * Sort grid's column and verify its ascending order.
		 */
		@Test
		public void testSort(){
			DesktopAgent desktop = Zats.newClient().connect("/~./basic/group-sort.zul");

			//column
			ComponentAgent sortingColumn = desktop.query("column[label='"+COLUMN_AUTHOR+"']");
			Label sortStatus = desktop.query("#sortStatus").as(Label.class);

			sortingColumn.as(SortAgent.class).sort(true);
			Assert.assertEquals(COLUMN_AUTHOR+",true", sortStatus.getValue());
			sortingColumn.as(SortAgent.class).sort(false);
			Assert.assertEquals(COLUMN_AUTHOR+",false", sortStatus.getValue());

			sortingColumn = desktop.query("column[label='"+COLUMN_TITLE+"']");

			sortingColumn.as(SortAgent.class).sort(true);
			Assert.assertEquals(COLUMN_TITLE+",true", sortStatus.getValue());
			sortingColumn.as(SortAgent.class).sort(false);
			Assert.assertEquals(COLUMN_TITLE+",false", sortStatus.getValue());

			//listheader
			ComponentAgent sortingHeader =  desktop.query("listheader[label='Name']");
			Assert.assertEquals(SwitchedSortAgentImpl.DESCENDING, sortingHeader.as(Listheader.class).getSortDirection());
			//can sort in specified order in spite of its original sorting order
			sortingHeader.as(SortAgent.class).sort(true);
			Assert.assertEquals("Name", sortStatus.getValue());
			Assert.assertEquals(SwitchedSortAgentImpl.ASCENDING, sortingHeader.as(Listheader.class).getSortDirection());
			sortingHeader.as(SortAgent.class).sort(false);
			Assert.assertEquals(SwitchedSortAgentImpl.DESCENDING, sortingHeader.as(Listheader.class).getSortDirection());
			//repeat sorting in the same order should work correctly
			sortingHeader.as(SortAgent.class).sort(false);
			Assert.assertEquals(SwitchedSortAgentImpl.DESCENDING, sortingHeader.as(Listheader.class).getSortDirection());

			sortingHeader =  desktop.query("listheader[label='Gender']");
			sortingHeader.as(SortAgent.class).sort(false);
			Assert.assertEquals(SwitchedSortAgentImpl.DESCENDING, sortingHeader.as(Listheader.class).getSortDirection());
			Assert.assertEquals("Gender", sortStatus.getValue());

			//treecol
			sortingColumn = desktop.query("treecol[label='Description']");

			//can sort in specified order in spite of its original sorting order
			sortingColumn.as(SortAgent.class).sort(false);
			Assert.assertEquals("Description", sortStatus.getValue());
			Assert.assertEquals(SwitchedSortAgentImpl.DESCENDING, sortingColumn.as(Treecol.class).getSortDirection());
			sortingColumn.as(SortAgent.class).sort(true);
			Assert.assertEquals(SwitchedSortAgentImpl.ASCENDING, sortingColumn.as(Treecol.class).getSortDirection());
			//repeat sorting in the same order should work correctly
			sortingColumn.as(SortAgent.class).sort(true);
			Assert.assertEquals(SwitchedSortAgentImpl.ASCENDING, sortingColumn.as(Treecol.class).getSortDirection());

		}

	private String fetchString(InputStream is) throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			Reader r = new InputStreamReader(is);
			r = new BufferedReader(r);
			int c;
			while ((c = r.read()) >= 0)
				sb.append((char) c);
		} finally {
			Util.close(is);
		}
		return sb.toString();
	}

	@Test
	public void testDownload() throws Exception {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/download.zul");
		assertTrue(desktop.query("#dummy").is(Button.class));
		assertTrue(desktop.query("#btn0").is(Button.class));
		assertTrue(desktop.query("#btn1").is(Button.class));
		assertTrue(desktop.query("#btn2").is(Button.class));
		// temp file
		String path = desktop.query("#path").as(Label.class).getValue();
		assertTrue(path != null && path.length() > 0);
		File temp = new File(path);
		assertTrue(temp.canRead());

		// no download
		assertTrue(desktop.getDownloadable() == null);
		desktop.query("#dummy").click();
		assertTrue(desktop.getDownloadable() == null);

		// download from file
		desktop.query("#btn0").click();
		Resource downloadable = desktop.getDownloadable();
		assertTrue(downloadable != null);
		assertEquals(temp.getName(), downloadable.getName());
		assertEquals("Hello ZK!\nThis is a test file!", fetchString(downloadable.getInputStream()));

		// no download again
		desktop.query("#dummy").click();
		assertTrue(desktop.getDownloadable() == null);

		// download from data
		desktop.query("#btn1").click();
		downloadable = desktop.getDownloadable();
		assertTrue(downloadable != null);
		assertEquals("test.txt", downloadable.getName());
		assertEquals("Hello world!\nHello ZK!", fetchString(downloadable.getInputStream()));

		// download from file and resumable
		desktop.query("#btn2").click();
		downloadable = desktop.getDownloadable();
		assertTrue(downloadable != null);
		assertEquals(temp.getName(), downloadable.getName());
		assertEquals("Hello ZK!\nThis is a test file!", fetchString(downloadable.getInputStream()));

		// download last file (invoke download twice in one AU event)
		desktop.query("#btn3").click();
		downloadable = desktop.getDownloadable();
		assertTrue(downloadable != null);
		assertEquals("file1.txt", downloadable.getName());
		assertEquals("This is no. 1!", fetchString(downloadable.getInputStream()));
	}

	@Test
	public void testDownload2() throws Exception {
		// download file at "doAfterComposer()"
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/download2.zul");
		Resource downloadable = desktop.getDownloadable();
		assertTrue(downloadable != null);
		assertEquals("Hello ZK!\nThis is a test file!", fetchString(downloadable.getInputStream()));
	}

	@Test
	public void testRichlet() {
		DefaultZatsEnvironment env = new DefaultZatsEnvironment("./src/test/resources/web/WEB-INF");
		try {
			env.init("./src/test/resources/web");
			DesktopAgent desktop = env.newClient().connect("/zk/test");
			Label msg = desktop.query("#msg").as(Label.class);
			Assert.assertEquals("Hello world!", msg.getValue());
			desktop.query("#btn").click();
			Assert.assertEquals("Hello ZK!", msg.getValue());
		} finally {
			env.destroy();
		}
	}

	@Test
	public void testAuAgent() {

		// click test
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/click.zul");
		assertEquals("Hello World!", desktopAgent.query("#msg").as(Label.class).getValue());

		//	desktopAgent.query("#btn").as(ClickAgent.class).click();
		AuData au = new AuData(Events.ON_CLICK).setData("x", 0).setData("y", 0).setData("pageX", 0)
				.setData("pageY", 0);
		desktopAgent.query("#btn").as(AuAgent.class).post(au);

		assertEquals("Welcome", desktopAgent.query("#msg").as(Label.class).getValue());

		// select test
		Zats.cleanup();
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/select.zul");

		Label selected = desktop.query("#selected").as(Label.class);
		assertEquals("", selected.getValue());

		// combobox
		String[] labels = new String[] { "cbi1", "cbi2", "cbi3" };
		List<ComponentAgent> cbitems = desktop.queryAll("#cb > comboitem");
		assertEquals(labels.length, cbitems.size());
		for (int i = 0; i < labels.length; ++i) {
			ComponentAgent target = cbitems.get(i);
			ComponentAgent parent = target.getParent();
			au = new AuData(Events.ON_SELECT).setData("items", new Object[] { target.getUuid() }).setData(
					"reference", parent.getUuid());
			parent.as(AuAgent.class).post(au);
			assertEquals(labels[i], selected.getValue());
		}
	}


	@Test
	public void testEchoEvent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/~./basic/echo.zul");

		// echo events at doAfterComposer()
		ComponentAgent lblX = desktopAgent.query("#lblX");
		assertNotNull(lblX);
		assertEquals("Bar", lblX.as(Label.class).getValue());
		ComponentAgent lblY = desktopAgent.query("#lblY");
		assertNotNull(lblY);
		assertEquals("Bar2", lblY.as(Label.class).getValue());

		// immediate echo events
		Label lbl11 = desktopAgent.query("#lbl11").as(Label.class);
		Label lbl12 = desktopAgent.query("#lbl12").as(Label.class);
		Label lbl13 = desktopAgent.query("#lbl13").as(Label.class);
		assertEquals("", lbl11.getValue());
		assertEquals("", lbl12.getValue());
		assertEquals("", lbl13.getValue());
		assertFalse("incorrect".equals(lbl11.getValue()));

		ComponentAgent btn1 = desktopAgent.query("#btn1");
		btn1.click();
		assertEquals("MyEcho", lbl11.getValue());
		assertEquals("YourEcho", lbl12.getValue());
		assertEquals("ItsEcho", lbl13.getValue());
		btn1.click();
		assertEquals("MyEchoMyEcho", lbl11.getValue());
		assertEquals("YourEchoYourEcho", lbl12.getValue());
		assertEquals("ItsEchoItsEcho", lbl13.getValue());

		// immediate echo events without data
		Label lbl21 = desktopAgent.query("#lbl21").as(Label.class);
		Label lbl22 = desktopAgent.query("#lbl22").as(Label.class);
		Label lbl23 = desktopAgent.query("#lbl23").as(Label.class);
		assertEquals("", lbl21.getValue());

		ComponentAgent btn2 = desktopAgent.query("#btn2");
		btn2.click();
		assertEquals("MyEcho2", lbl21.getValue());
		assertEquals("YourEcho2", lbl22.getValue());
		assertEquals("ItsEcho2", lbl23.getValue());
		btn2.click();
		assertEquals("MyEcho2MyEcho2", lbl21.getValue());
		assertEquals("YourEcho2YourEcho2", lbl22.getValue());
		assertEquals("ItsEcho2ItsEcho2", lbl23.getValue());

		// loop echo with normal operations - immediate mode
		Label lbl31 = desktopAgent.query("#lbl31").as(Label.class);
		Label lbl32 = desktopAgent.query("#lbl32").as(Label.class);
		Label lbl4 = desktopAgent.query("#lbl4").as(Label.class);
		assertEquals("", lbl31.getValue());
		assertEquals("", lbl32.getValue());
		assertEquals("", lbl4.getValue());

		desktopAgent.query("#btn3").click();
		assertEquals("3", lbl31.getValue());
		assertEquals("4", lbl32.getValue());
		assertEquals("", lbl4.getValue());
		desktopAgent.query("#btn4").click();
		assertEquals("3", lbl31.getValue());
		assertEquals("4", lbl32.getValue());
		assertEquals("HelloEcho", lbl4.getValue());

		// loop echo with normal operations - piggyback mode
		desktopAgent.getClient().setEchoEventMode(EchoEventMode.PIGGYBACK);

		String hellos = "HelloEcho";
		desktopAgent.query("#btn3").click();
		assertEquals("0", lbl31.getValue());
		assertEquals("0", lbl32.getValue());
		assertEquals(hellos, lbl4.getValue());

		String[] a31 = { "1", "2", "3", "3", "3", "3" };
		String[] a32 = { "1", "2", "3", "4", "4", "4" };
		for (int i = 0; i < a31.length; ++i) {
			desktopAgent.query("#btn4").click();
			assertEquals(a31[i], lbl31.getValue());
			assertEquals(a32[i], lbl32.getValue());
			assertEquals(hellos += "HelloEcho", lbl4.getValue());
		}
	}
}
