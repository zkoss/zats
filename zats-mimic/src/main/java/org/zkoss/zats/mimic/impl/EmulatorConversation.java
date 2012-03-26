/* EmulatorConversation.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by Pao Wang

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.eclipse.jetty.util.UrlEncoded;
import org.zkoss.json.JSONValue;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversation;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;
import org.zkoss.zk.ui.Desktop;

/**
 * The test conversation implemented by server emulator.
 * 
 * @author pao
 */
public class EmulatorConversation implements Conversation {
	private static Logger logger;
	private Emulator emulator;
	private File web;
	private DesktopAgent desktopAgent;
	private List<String> cookies;

	public EmulatorConversation() {
		logger = Logger.getLogger(EmulatorConversation.class.getName());
		cookies = new LinkedList<String>();
		// prepare environment
		String tmpDir = System.getProperty("java.io.tmpdir", ".");
		File webinf = new File(tmpDir, "zats/" + System.currentTimeMillis()
				+ "/WEB-INF");
		if (!webinf.mkdirs())
			throw new ConversationException("can't create temp directory");
		web = webinf.getParentFile();
		File zk = new File(web, "WEB-INF/zk.xml");
		copy(EmulatorConversation.class.getResourceAsStream("WEB-INF/zk.xml"),
				zk);
		// TODO clean directories
		zk.deleteOnExit();
		webinf.deleteOnExit();
	}

	public void start(String resourceRoot) {
		// create emulator
		emulator = new EmulatorBuilder(web)
				.addResource(resourceRoot)
				.descriptor(
						EmulatorConversation.class
								.getResource("WEB-INF/web.xml")).create();
	}

	public synchronized void stop() {
		try {
			if (emulator == null)
				emulator.close();
		} finally {
			emulator = null;
		}
	}

	public void open(String zulPath) {
		InputStream is = null;
		try {
			// load zul page
			HttpURLConnection huc = getConnection(zulPath, "GET");
			huc.connect();
			// TODO, read response, handle redirect.

			cookies = huc.getHeaderFields().get("Set-Cookie");
			is = huc.getInputStream();
			if (logger.isLoggable(Level.FINEST))
				logger.finest(getReplyString(is, huc.getContentEncoding()));
			// get specified objects such as Desktop
			Desktop desktop = (Desktop) emulator.getRequestAttributes().get(
					"javax.zkoss.zk.ui.desktop");
			// TODO, what if a non-zk(zul) page, throw exception?
			desktopAgent = new DefaultDesktopAgent(this, desktop);
		} catch (Exception e) {
			throw new ConversationException("", e);
		} finally {
			close(is);
		}
	}

	public void clean() {
		// clean desktop
		InputStream is = null;
		try {
			if (desktopAgent != null) {
				// use au to remove a desktop
				String url = MessageFormat.format(
						"/zkau?dtid={0}&cmd_0=rmDesktop&opt_0=i",
						desktopAgent.getId());
				HttpURLConnection huc = getConnection(url, "GET");
				huc.connect();
				is = huc.getInputStream();
				if (logger.isLoggable(Level.FINEST))
					logger.finest(getReplyString(is, "utf-8"));
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "", e);
		} finally {
			Util.close(is);
			desktopAgent = null;
			cookies = new LinkedList<String>();
		}
	}

	public DesktopAgent getDesktop() {
		return desktopAgent;
	}

	public HttpSession getSession() {
		if (desktopAgent == null)
			return null;
		return (HttpSession) desktopAgent.getDesktop().getSession().getNativeSession();
	}

	public void postUpdate(ComponentAgent target, String cmd,
			Map<String, Object> data) {
		// prepare au data
		String dtid = UrlEncoded.encodeString(desktopAgent.getId());
		cmd = UrlEncoded.encodeString(cmd);
		String uuid = UrlEncoded.encodeString(target.getUuid());
		String param;
		if (data != null && data.size() > 0) {
			String jsonData = UrlEncoded.encodeString(JSONValue
					.toJSONString(data));
			param = MessageFormat.format(
					"dtid={0}&cmd_0={1}&uuid_0={2}&data_0={3}", dtid, cmd,
					uuid, jsonData);
		} else
			param = MessageFormat.format("dtid={0}&cmd_0={1}&uuid_0={2}", dtid,
					cmd, uuid);

		if (logger.isLoggable(Level.FINEST)) {
			String id = target.getId() != null ? "id " + target.getId() : "uuid " + target.getUuid();
			logger.finest(id + " perform AU: " + UrlEncoded.decodeString(param, 0, param.length(), "utf-8"));
		}
		
		OutputStream os = null;
		InputStream is = null;
		try {
			// create http request and perform it
			HttpURLConnection c = getConnection("/zkau", "POST");
			c.setDoOutput(true);
			c.setDoInput(true);
			for (String cookie : cookies)
				c.addRequestProperty("Cookie", cookie.split(";", 2)[0]); // handle
																			// cookie
																			// for
																			// session
			c.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			c.connect();
			os = c.getOutputStream();
			os.write(param.getBytes("utf-8"));
			close(os);
			// TODO, read response, handle redirect.
			// read response
			is = c.getInputStream();
			if (logger.isLoggable(Level.FINEST))
				logger.finest(getReplyString(is, c.getContentEncoding()));
		} catch (Exception e) {
			throw new ConversationException("", e);
		} finally {
			close(os);
			close(is);
		}
	}

	private HttpURLConnection getConnection(String path, String method) {
		try {
			URL url = new URL(emulator.getAddress() + path);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod(method);
			huc.setUseCaches(false);
			huc.addRequestProperty("Host",
					emulator.getHost() + ":" + emulator.getPort());
			huc.addRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			huc.addRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			huc.addRequestProperty("Accept-Language",
					"zh-tw,en-us;q=0.7,en;q=0.3");
			return huc;
		} catch (Exception e) {
			throw new ConversationException("", e);
		}
	}

	private void copy(InputStream src, File dest) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(dest);
			byte[] buf = new byte[65536];
			int len;
			while (true) {
				len = src.read(buf);
				if (len < 0)
					break;
				os.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new ConversationException("fail to copy file", e);
		} finally {
			close(src);
			close(os);
		}
	}

	private void close(Closeable c) {
		try {
			c.close();
		} catch (Throwable e) {
		}
	}

	private String getReplyString(InputStream is, String encoding) {
		String reply = null;
		Reader r = null;
		try {
			StringBuilder sb = new StringBuilder();
			r = new BufferedReader(new InputStreamReader(is,
					encoding != null ? encoding : "ISO-8859-1"));
			while (true) {
				int c = r.read();
				if (c < 0)
					break;
				sb.append((char) c);
			}
			reply = sb.toString();
		} catch (Exception e) {
			logger.log(Level.WARNING, "", e);
		} finally {
			close(r);
		}
		return reply;
	}
}
