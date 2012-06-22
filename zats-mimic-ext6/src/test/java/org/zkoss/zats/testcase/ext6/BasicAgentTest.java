/* BasicAgentTest.java

	Purpose:
		
	Description:
		
	History:
		Mar 27, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.testcase.ext6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.jetty.util.TypeUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.OpenAgent;
import org.zkoss.zats.mimic.operation.SelectByIndexAgent;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

/**
 * @author pao
 * 
 */
public class BasicAgentTest {
	@BeforeClass
	public static void init() {
		Zats.init(".");
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}

	@Test
	public void testToolbarButtonCheck() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/toolbar.zul");

		for (int i = 1; i <= 6; ++i)
			assertEquals("tbb" + i, desktop.query("#tbb" + i).as(Toolbarbutton.class).getLabel());

		Label clicked = desktop.query("#clicked").as(Label.class);
		assertEquals("", clicked.getValue());

		for (int i = 1; i <= 6; ++i) {
			desktop.query("#tbb" + i).click();
			assertEquals("tbb" + i, clicked.getValue());
		}

		Label checked = desktop.query("#checked").as(Label.class);
		assertEquals("", checked.getValue());

		String[] values = { "tbb4 ", "tbb4 tbb5 ", "tbb4 tbb5 tbb6 ", "tbb4 tbb6 " };
		for (int i = 4; i <= 6; ++i) {
			desktop.query("#tbb" + i).check(true);
			assertEquals(values[i - 4], checked.getValue());
			assertEquals("tbb6", clicked.getValue()); // "check" should not perform "click"
		}

		desktop.query("#tbb5").check(false);
		assertEquals(values[3], checked.getValue());
	}
	
	@Test
	public void testOpenAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/open-ext6.zul");

		Label open = desktop.query("#open").as(Label.class);
		Label close = desktop.query("#close").as(Label.class);
		assertEquals("", open.getValue());
		assertEquals("", close.getValue());

		String values[] = { "", "" };
		// combobutton
		String id = "#aCombobutton";
		values[0] = id.substring(1);
		desktop.query(id).as(OpenAgent.class).open(true);
		assertEquals(values[0], open.getValue());
		assertEquals(values[1], close.getValue());
		values[1] = id.substring(1);
		desktop.query(id).as(OpenAgent.class).open(false);
		assertEquals(values[0], open.getValue());
		assertEquals(values[1], close.getValue());
	}
	
	@Test
	public void testSelectByIndexAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/selectbox.zul");
		
		Label msg = desktop.query("#msg").as(Label.class);
		assertEquals("", msg.getValue());

		SelectByIndexAgent sb = desktop.query("#sb").as(SelectByIndexAgent.class);
		for (int i = 3; i >= 1; --i) {
			sb.select(i - 1);
			assertEquals("item" + i, msg.getValue());
		}
	}
	
	@Test
	public void testClickAll() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/click-ext6.zul");

		Label target = desktop.query("#target").as(Label.class);
		Label event = desktop.query("#eventName").as(Label.class);
		assertEquals("", target.getValue());
		assertEquals("", event.getValue());

		ComponentAgent comps = desktop.query("#comps");
		assertNotNull(comps);

		String[] names = { "combobutton", "idspace" };
		for (String name : names) {
			ClickAgent agent = comps.query(name).as(ClickAgent.class);
			agent.click();
			assertEquals(name, target.getValue());
			assertEquals(Events.ON_CLICK, event.getValue());
			agent.doubleClick();
			assertEquals(name, target.getValue());
			assertEquals(Events.ON_DOUBLE_CLICK, event.getValue());
			agent.rightClick();
			assertEquals(name, target.getValue());
			assertEquals(Events.ON_RIGHT_CLICK, event.getValue());
		}
	}
	
	@Test
	public void testMultipleFilesInSingleUpload() throws Exception {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/upload-ext6.zul");
		Vbox results = desktop.query("#results").as(Vbox.class);
		Assert.assertEquals(0, results.getChildren().size());

		// prepare files for testing 
		File textFile = File.createTempFile("zats-upload-text-", ".tmp");
		textFile.deleteOnExit();
		String text = "Hello! World!\r\nHello! ZK!\r\n";
		byte[] textRaw = text.getBytes("ISO-8859-1");
		String textBinary = TypeUtil.toHexString(textRaw).toUpperCase();
		FileOutputStream fos = new FileOutputStream(textFile);
		fos.write(textRaw);
		fos.close();
		File imageFile = File.createTempFile("zats-upload-image-", ".png");
		imageFile.deleteOnExit();
		byte[] imageRaw = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 10, 0,
				0, 0, 10, 8, 2, 0, 0, 0, 2, 80, 88, -22, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, -79, -113, 11, -4, 97, 5,
				0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 18, 116, 0, 0, 18, 116, 1, -34, 102, 31, 120, 0, 0, 0, 39, 73, 68,
				65, 84, 40, 83, 99, 124, 43, -93, -62, -128, 4, 76, -89, 106, 32, 115, -103, -112, 57, -104, 108, -102,
				74, 51, 42, 109, -12, 65, -74, 82, 118, 122, -61, 96, 113, 26, 0, -35, -38, 4, -123, -73, -75, -2, 83,
				0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126 };
		String imageBinary = TypeUtil.toHexString(imageRaw).toUpperCase();
		fos = new FileOutputStream(imageFile);
		fos.write(imageRaw);
		fos.close();

		for (int i = 0; i < 4; ++i) {
			// clean
			desktop.query("#clean").click();
			Assert.assertEquals(0, desktop.query("#results").getChildren().size());
			
			// upload 3 files
			UploadAgent agent = desktop.query("#btn" + i).as(UploadAgent.class);
			agent.upload(textFile, "text/plain");
			agent.upload(textFile, "application/octet-stream");
			agent.upload(imageFile, "image/png");
			agent.finish();
			Assert.assertEquals(3, desktop.query("#results").getChildren().size());
			
			// text
			Assert.assertEquals(textFile.getName(), desktop.query("#file0 .name").as(Label.class).getValue());
			Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
			Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file0 .binary").as(Label.class).getValue());
			Assert.assertEquals(text, desktop.query("#file0 .text").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file0 .width").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file0 .height").as(Label.class).getValue());
			// binary
			Assert.assertEquals(textFile.getName(), desktop.query("#file1 .name").as(Label.class).getValue());
			Assert.assertEquals("application/octet-stream", desktop.query("#file1 .contentType").as(Label.class).getValue());
			Assert.assertEquals("octet-stream", desktop.query("#file1 .format").as(Label.class).getValue());
			Assert.assertEquals(textBinary, desktop.query("#file1 .binary").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file1 .text").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file1 .width").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file1 .height").as(Label.class).getValue());
			// image
			Assert.assertEquals(imageFile.getName(), desktop.query("#file2 .name").as(Label.class).getValue());
			Assert.assertEquals("image/png", desktop.query("#file2 .contentType").as(Label.class).getValue());
			Assert.assertEquals("png", desktop.query("#file2 .format").as(Label.class).getValue());
			Assert.assertEquals(imageBinary, desktop.query("#file2 .binary").as(Label.class).getValue());
			Assert.assertEquals("", desktop.query("#file2 .text").as(Label.class).getValue());
			Assert.assertEquals("10px", desktop.query("#file2 .width").as(Label.class).getValue());
			Assert.assertEquals("10px", desktop.query("#file2 .height").as(Label.class).getValue());
		}
	}
}
