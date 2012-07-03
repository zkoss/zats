/* EmulatorClient.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.util.UrlEncoded;
import org.zkoss.json.JSONValue;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.common.json.parser.ParseException;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zk.ui.Desktop;

/**
 * The default server emulator client implementation.
 * @author pao
 */
public class EmulatorClient implements Client, ClientCtrl {
	private static Logger logger = Logger.getLogger(EmulatorClient.class.getName());
	private Emulator emulator;
	private Map<String, DesktopAgent> desktopAgents = new HashMap<String, DesktopAgent>();
	private Map<String, String> cookies = new HashMap<String, String>();
	private DestroyListener destroyListener;
	private Map<String, List<String>> auQueues; // AU queues of desktops

	public EmulatorClient(Emulator emulator) {
		this.emulator = emulator;
		this.auQueues = new ConcurrentHashMap<String, List<String>>();
	}
	
	public DesktopAgent connectAsIncluded(String zulPath, Map<String, Object> args) {

		// generate key and map for transferring data into server side
		String key = Long.toString(System.nanoTime(), 36);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("url", zulPath);
		data.put("args", args);
		emulator.getServletContext().setAttribute(key, data);

		// connect to adapter with key
		String adapter = "/~./impl/includingAdapter.zul?id=" + key;
		DesktopAgent desktop = connect(adapter);

		// clean resources
		emulator.getServletContext().removeAttribute(key);
		data.clear();

		return desktop;
	}

	public DesktopAgent connect(String zulPath) {
		InputStream is = null;
		try {
			// load zul page
			HttpURLConnection huc = getConnection(zulPath, "GET");
			huc.connect();
			// read response and pass to response handlers
			fetchCookies(huc);
			is = huc.getInputStream();
			String raw = getReplyString(is, huc.getContentEncoding());
			List<LayoutResponseHandler> handlers = ResponseHandlerManager.getInstance().getLayoutResponseHandlers();
			for (LayoutResponseHandler h : handlers) {
				try {
					h.process(this, raw);
				} catch (Throwable e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("HTTP response header: " + huc.getHeaderFields());
				logger.finest("HTTP response content: " + raw);
			}

			// get specified objects such as Desktop
			Desktop desktop = (Desktop) emulator.getRequestAttributes().get("javax.zkoss.zk.ui.desktop");
			// TODO, what if a non-zk(zul) page, throw exception?
			DesktopAgent desktopAgent = new DefaultDesktopAgent(this, desktop);
			desktopAgents.put(desktopAgent.getId(), desktopAgent);
			return desktopAgent;
		} catch (Exception e) {
			throw new ZatsException(e.getMessage(), e);
		} finally {
			close(is);
		}
	}

	public void destroy() {
		if (destroyListener != null) {
			destroyListener.willDestroy(this);
		}

		for (DesktopAgent d : desktopAgents.values()) {
			destroy(d);
		}
		desktopAgents.clear();

		// should be after cleaning desktop
		cookies.clear();
		auQueues.clear();
	}

	public void destroy(DesktopAgent desktopAgent) {
		postUpdate(desktopAgent.getId(),"rmDesktop",null,null,"i");
		flush(desktopAgent.getId());
	}
	
	public void postUpdate(String desktopId, String command, String targetUUID, Map<String, Object> data,String option) {
		if(desktopId==null){
			throw new IllegalArgumentException("desktop id is null");
		}else if(command == null){
			throw new IllegalArgumentException("command is null");
		}
		
		// get or create AU queue
		List<String> queue = auQueues.get(desktopId);
		if (queue == null) {
			queue = new LinkedList<String>();
			auQueues.put(desktopId, queue);
		}
		// prepare AU data and queue it (without desktop ID)
		final int index = queue.size();
		final String cmd = command = UrlEncoded.encodeString(command);
		final StringBuilder param = new StringBuilder();
		param.append("&cmd_").append(index).append("=").append(cmd);
		if (targetUUID != null) {
			String uuid = UrlEncoded.encodeString(targetUUID);
			param.append("&uuid_").append(index).append("=").append(uuid);
		}
		if (data != null && data.size() > 0) {
			String jsonData = UrlEncoded.encodeString(JSONValue.toJSONString(data));
			param.append("&data_").append(index).append("=").append(jsonData);
		}
		if (option != null && option.length() > 0) {
			option = UrlEncoded.encodeString(option);
			param.append("&opt_").append(index).append("=").append(option);
		}
		queue.add(param.toString());
		
		// debug log
		if (logger.isLoggable(Level.FINEST)) {
			String content = param.toString();
			logger.finest(targetUUID + " perform AU: " + UrlEncoded.decodeString(content, 0, content.length(), "utf-8"));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void flush(String desktopId) {
		OutputStream os = null;
		InputStream is = null;
		try {

			// combine AU events from queue into single request
			StringBuilder sb = new StringBuilder();
			sb.append("dtid=").append(UrlEncoded.encodeString(desktopId)); // desktop ID.
			List<String> queue = auQueues.get(desktopId);
			if (queue == null || queue.size() <= 0) // do nothing
				return;
			for (String cmd : queue)
				sb.append(cmd);
			queue.clear();
			final String content = sb.toString();

			// create http request and perform it
			HttpURLConnection c = getConnection("/zkau", "POST");
			c.setDoOutput(true);
			c.setDoInput(true);
			c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("HTTP request header: " + c.getRequestProperties());
				logger.finest("HTTP request content: " + content);
			}
			c.connect();
			os = c.getOutputStream();
			os.write(content.getBytes("utf-8"));
			close(os);

			// read and parse response, and pass to response handlers
			fetchCookies(c);
			is = c.getInputStream();
			String raw = getReplyString(is, c.getContentEncoding());
			Map<String, Object> json = (Map<String, Object>) org.zkoss.zats.common.json.JSONValue.parseWithException(raw);
			List<UpdateResponseHandler> handlers = ResponseHandlerManager.getInstance().getUpdateResponseHandlers();
			for (UpdateResponseHandler h : handlers) {
				try {
					h.process(desktopAgents.get(desktopId), json);
				} catch (Throwable e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("HTTP response header: " + c.getHeaderFields());
				logger.finest("HTTP response content: " + raw);
			}
			
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "unexpect exception when parsing JSON", e);
		} catch (Exception e) {
			throw new ZatsException(e.getMessage(), e);
		} finally {
			close(os);
			close(is);
		}
	}

	public HttpURLConnection getConnection(String path, String method) {
		try {
			URL url = new URL(emulator.getAddress() + path);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod(method);
			huc.setUseCaches(false);
			huc.addRequestProperty("Host", emulator.getHost() + ":" + emulator.getPort());
			huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
			// handle cookies
			for (Entry<String, String> cookie : cookies.entrySet()) {
				String value = cookie.getValue() != null ? cookie.getValue() : "";
				huc.addRequestProperty("Cookie", cookie.getKey() + "=" + value);
			}
			return huc;
		} catch (Exception e) {
			throw new ZatsException(e.getMessage(), e);
		}
	}
	
	public InputStream openConnection(String path) throws IOException {
		HttpURLConnection c = getConnection(path, "GET");
		c.setDoOutput(false);
		c.setDoInput(true);
		return c.getInputStream();
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
			r = new BufferedReader(new InputStreamReader(is, encoding != null ? encoding : "ISO-8859-1"));
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

	public void setDestroyListener(DestroyListener l) {
		destroyListener = l;
	}

	@SuppressWarnings("deprecation")
	private void fetchCookies(HttpURLConnection connection)
	{
		// fetch and parse cookies from connection
		List<String> list = connection.getHeaderFields().get("Set-Cookie");
		if (list == null)
			return;
		for (String raw : list) {
			try {

				// parse cookie and append to the collection of cookies 
				String[] tokens = raw.trim().split(";"); // fetch cookie
				tokens = tokens[0].split("="); // fetch key and value from cookie
				cookies.put(tokens[0], tokens.length < 2 ? "" : tokens[1]); // value can be null

				// get cookie attributes
				byte[] data = raw.replaceAll(";", "\n").getBytes("ASCII");
				Properties attr = new Properties();
				attr.load(new ByteArrayInputStream(data));

				// check expired time and remove cookie if necessary 
				String expired = attr.getProperty("Expires");
				if (expired != null && expired.length() > 0) {
					try {
						long time = Date.parse(expired); // W3C Datetime Format
						if (time < System.currentTimeMillis())
							cookies.remove(tokens[0]);
					} catch (Throwable e) {
						logger.log(Level.WARNING, "unexpect exception when parsing HTTP Datetime string", e);
					}
				}
			} catch (Exception e) {
				new ZatsException("unexpected exception", e);
			}
		}
	}
	
	public void setCookie(String key, String value) {
		if (key == null || key.startsWith("$"))
			throw new IllegalArgumentException(key == null ? "cookie key name can't be null"
					: "cookie key name can't be start with '$'");
		if (value != null)
			cookies.put(key, value);
		else
			cookies.remove(key);
	}

	public String getCookie(String key) {
		if (key == null)
			throw new IllegalArgumentException("cookie key name can't be null");
		return cookies.get(key);
	}

	public Map<String, String> getCookies() {
		return new HashMap<String, String>(cookies); // a copy of cookies
	}
}
