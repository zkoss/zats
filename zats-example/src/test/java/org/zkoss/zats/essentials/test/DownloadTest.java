package org.zkoss.zats.essentials.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Resource;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.impl.Util;

public class DownloadTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp");
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
	public void test() throws Exception{
		DesktopAgent desktop = Zats.newClient().connect("/essentials/download.zul");
		Assert. assertNull (desktop.getDownloadable());
		desktop.query("#btn").click();
		Resource resource = desktop.getDownloadable();
		Assert.assertNotNull(resource);
		Assert.assertEquals("hello.txt", resource.getName());
		String content = readFileContent(resource.getInputStream());
		Assert.assertEquals("Hello world!", content);
	}
	
	private String readFileContent(InputStream is) throws Exception {
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
}
