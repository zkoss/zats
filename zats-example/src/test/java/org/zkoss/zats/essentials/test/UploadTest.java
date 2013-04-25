package org.zkoss.zats.essentials.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zul.Label;

public class UploadTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp"); // user can load by
													// configuration file
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
	public void testUploadAttribute() throws Exception{
		File file = getFile();
		DesktopAgent desktop = Zats.newClient().connect("/essentials/upload.zul");
		UploadAgent agent = desktop.query("#btn").as(UploadAgent.class);
		agent.upload(file, "text/plain");
		agent.finish();
		//verify result
		Assert.assertEquals("sample.txt", desktop.query("#file0 .name").as(Label.class).getValue());
		Assert.assertEquals("text/plain", desktop.query("#file0 .contentType").as(Label.class).getValue());
		Assert.assertEquals("txt", desktop.query("#file0 .format").as(Label.class).getValue());
		Assert.assertEquals("", desktop.query("#file0 .binary").as(Label.class).getValue());
		Assert.assertEquals("", desktop.query("#file0 .width").as(Label.class).getValue());
		Assert.assertEquals("", desktop.query("#file0 .height").as(Label.class).getValue());
	}
	
	private File getFile() throws Exception{
		String text = "Hello! World!\r\nHello! ZK!\r\n";
		File file = new File("sample.txt");
		file.createNewFile();
		file.deleteOnExit();
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(text);
		return file;
	}
	
	@Test
	public void testUploadMethod() throws Exception{
	    File[] files = getFiles();
	    DesktopAgent desktop = Zats.newClient().connect("/essentials/upload.zul");
	    desktop.query("#label2").click();
	    UploadAgent agent = desktop.as(UploadAgent.class);
	    agent.upload(files[0], "text/plain");
	    agent.upload(files[1], "image/png");
	    agent.finish();
	    //verify result
	    Assert.assertEquals("sample.txt", desktop.query("#file0 .name").as(Label.class).getValue());
	    
	    Assert.assertEquals("sample.png", desktop.query("#file1 .name").as(Label.class).getValue());
	}
	
	private File[] getFiles() throws Exception{
		File[] files = new File[2];
		files[0] = getFile();
		
		// image stream
		byte[] ImageRaw = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 10, 0, 0, 0, 10, 8,
				2, 0, 0, 0, 2, 80, 88, -22, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9,
				112, 72, 89, 115, 0, 0, 18, 116, 0, 0, 18, 116, 1, -34, 102, 31, 120, 0, 0, 0, 39, 73, 68, 65, 84, 40,
				83, 99, 124, 43, -93, -62, -128, 4, 76, -89, 106, 32, 115, -103, -112, 57, -104, 108, -102, 74, 51, 42,
				109, -12, 65, -74, 82, 118, 122, -61, 96, 113, 26, 0, -35, -38, 4, -123, -73, -75, -2, 83, 0, 0, 0, 0,
				73, 69, 78, 68, -82, 66, 96, -126 };
		File file = new File("sample.png");
		file.createNewFile();
		file.deleteOnExit();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(ImageRaw);
		files[1] = file;
		return files;
	}
}

