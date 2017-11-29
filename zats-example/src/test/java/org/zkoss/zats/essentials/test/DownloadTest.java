package org.zkoss.zats.essentials.test;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Resource;
import org.zkoss.zats.mimic.impl.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DownloadTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void test() throws Exception{
		DesktopAgent desktop = autoClient.connect("/essentials/download.zul");
		Assert.assertNull (desktop.getDownloadable());
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
