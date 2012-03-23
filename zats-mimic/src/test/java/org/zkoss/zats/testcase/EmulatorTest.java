package org.zkoss.zats.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Test;
import org.zkoss.zats.mimic.impl.EmulatorConversation;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;

public class EmulatorTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Emulator emulator;

	@After
	public void after() throws Exception {
		emulator.close();
	}

	@Test
	public void test() throws Exception {
		File temp = new File(System.getProperty("java.io.tmpdir", "."), "" + System.currentTimeMillis());
		File dir1 = new File(temp, "dir1");
		File dir2 = new File(temp, "dir2");
		dir1.mkdirs();
		dir2.mkdirs();
		String html = "<html><body>hello</body></html>";
		copy(new ByteArrayInputStream(html.getBytes("ISO-8859-1")), new File(dir2, "index.html"));
		emulator = new EmulatorBuilder(dir1).descriptor(EmulatorTest.class.getResource("web.xml"))
				.addResource(dir2).create();

		Emulator e = emulator;
		URL url = new URL(e.getAddress() + "/echo");
		assertEquals("127.0.0.1", e.getHost());
		assertEquals(url.getHost(), e.getHost());
		assertEquals(url.getPort(), e.getPort());
		assertTrue(e.getPort() > 0);

		ServletContext sc = e.getServletContext();
		assertNotNull(sc);
		assertNull(sc.getAttribute("msg"));
		assertEquals(0, e.getRequestAttributes().size());
		assertNull(e.getSessionId());
		assertEquals(0, e.getRequestParameters().size());

		// first request
		String msg = "hello";
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("GET");
		huc.addRequestProperty("Host", e.getHost() + ":" + e.getPort());
		huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
		huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		huc.connect();
		InputStream is = huc.getInputStream();
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		assertEquals(msg, r.readLine());
		r.close();
		huc.disconnect();
		assertNotNull(e.getSessionId());
		assertEquals(msg, e.getSessionAttributes().get("msg"));
		assertEquals(msg, e.getRequestAttributes().get("msg"));
		assertEquals(msg, sc.getAttribute("msg"));

		// repeat request many times
		for (int i = 0; i < 10; ++i)
			check(e, "cool" + i, new URL(e.getAddress() + "/echo?msg=cool" + i));

		// test static html
		url = new URL(e.getAddress() + "/index.html");
		huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("GET");
		huc.addRequestProperty("Host", e.getHost() + ":" + e.getPort());
		huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
		huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		huc.connect();
		is = huc.getInputStream();
		r = new BufferedReader(new InputStreamReader(is));
		assertEquals(html, r.readLine());
		r.close();
		huc.disconnect();
	}

	private void copy(InputStream is, File dest) throws Exception {
		OutputStream os = new FileOutputStream(dest);
		byte[] buf = new byte[65536];
		int len;
		while (true) {
			len = is.read(buf);
			if (len < 0)
				break;
			os.write(buf, 0, len);
		}
		is.close();
		os.close();
	}

	private void check(Emulator e, String msg, URL url) throws Exception {
		System.out.println(url);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("GET");
		huc.addRequestProperty("Host", e.getHost() + ":" + e.getPort());
		huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
		huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		huc.connect();
		InputStream is = huc.getInputStream();
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		assertEquals(msg, r.readLine());
		r.close();
		huc.disconnect();
		assertEquals(msg, e.getServletContext().getAttribute("msg"));
		assertEquals(msg, e.getRequestAttributes().get("msg"));
		assertEquals(msg, e.getSessionAttributes().get("msg"));
		assertNotNull(e.getSessionId());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = request.getParameter("msg");
		if (msg == null || msg.length() <= 0)
			msg = "hello";
		getServletContext().setAttribute("msg", msg);
		request.getSession().setAttribute("msg", msg);
		request.setAttribute("msg", msg);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		PrintWriter w = response.getWriter();
		w.println(msg);
		w.close();
	}
}
