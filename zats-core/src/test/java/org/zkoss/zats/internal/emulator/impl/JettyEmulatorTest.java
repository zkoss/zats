package org.zkoss.zats.internal.emulator.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zkoss.zats.internal.emulator.Emulator;

public class JettyEmulatorTest extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private JettyEmulator emulator;

	@Before
	public void before() throws Exception
	{
		URL descriptor = JettyEmulatorTest.class.getResource("WEB-INF/web.xml");
		String contentPath = System.getProperty("java.io.tmpdir", ".");
		assertNotNull(descriptor);
		assertTrue(new File(contentPath).exists());
		emulator = new JettyEmulator(contentPath, descriptor.toString());
	}

	@After
	public void after() throws Exception
	{
		emulator.close();
	}

	@Test
	public void test() throws Exception
	{
		Emulator e = emulator;
		URL url = new URL(e.getAddress());
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

		String msg = "hello";
		HttpURLConnection huc = (HttpURLConnection)url.openConnection();
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

		url = new URL(e.getAddress() + "/?msg=cool");
		msg = "cool";
		huc = (HttpURLConnection)url.openConnection();
		huc.setRequestMethod("GET");
		huc.addRequestProperty("Host", e.getHost() + ":" + e.getPort());
		huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
		huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		huc.connect();
		is = huc.getInputStream();
		r = new BufferedReader(new InputStreamReader(is));
		assertEquals(msg, r.readLine());
		r.close();
		huc.disconnect();
		assertEquals(msg, sc.getAttribute("msg"));
		assertEquals(msg, e.getRequestAttributes().get("msg"));
		assertEquals(msg, e.getSessionAttributes().get("msg"));
		assertNotNull(e.getSessionId());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String msg = request.getParameter("msg");
		if(msg == null || msg.length() <= 0)
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
