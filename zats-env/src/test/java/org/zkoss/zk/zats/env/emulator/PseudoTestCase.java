package org.zkoss.zk.zats.env.emulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PseudoTestCase
{
	Emulator emulator;

	@Before
	public void setUp() throws Exception
	{
		emulator = new EmulatorBuilder()
			.configure("/myConfigurationFile.xml")
			.create();
	}

	@After
	public void tearDown() throws Exception
	{
		emulator.close();
	}

	@Test
	public void test() throws Exception
	{
		String address = emulator.getAddress();

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(address + "/myServlet");
		HttpResponse response = httpclient.execute(httpget);
		assertTrue(response.getStatusLine().getStatusCode() == 200);

		ServletContext context = emulator.getServletContext();
		assertNotNull(context.getAttribute("context"));
		HttpServletResponse servletResponse = emulator.getLastServletResponse();
		assertTrue(servletResponse.getStatus() == 200);
		HttpServletRequest request = emulator.getLastServletRequest();
		assertNotNull(request.getAttribute("request"));
		HttpSession session = request.getSession(false); // don't create new one
		assertNotNull(session.getAttribute("session"));

		byte[] json = "{cmd: 'hello'}".getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(json);
		InputStreamEntity reqEntity = new InputStreamEntity(is, json.length);
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true);

		HttpPost httppost = new HttpPost(address + "myServlet2");
		httppost.setEntity(reqEntity);
		response = httpclient.execute(httppost);
		assertTrue(response.getStatusLine().getStatusCode() == 200);
		is = response.getEntity().getContent();
		Map<?, ?> result = (Map<?, ?>)JSON.parse(new InputStreamReader(is));
		assertEquals("HELLO", result.get("result"));

		context = emulator.getServletContext();
		assertNotNull(context.getAttribute("hello"));
		request = emulator.getLastServletRequest();
		session = request.getSession(false); // don't create new one
		assertNotNull(session.getAttribute("hello"));
	}
}
