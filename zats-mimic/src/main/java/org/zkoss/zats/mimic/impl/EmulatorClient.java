/* EmulatorClient.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.util.UrlEncoded;
import org.zkoss.json.JSONValue;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zk.ui.Desktop;

/**
 * The server emulator client implement
 * @author pao
 */
public class EmulatorClient implements Client, ClientCtrl {
	private static Logger logger = Logger.getLogger(EmulatorClient.class.getName());
	private Emulator emulator;
	private List<DesktopAgent> desktopAgentList = new LinkedList<DesktopAgent>();
	private List<String> cookies;
	private DestroyListener destroyListener;

	public EmulatorClient(Emulator emulator) {
		this.emulator = emulator;
	}

	public DesktopAgent connect(String zulPath) {
		InputStream is = null;
		try {
			// load zul page
			HttpURLConnection huc = getConnection(zulPath, "GET");
			huc.connect();
			// TODO, read response, handle redirect.

			cookies = huc.getHeaderFields().get("Set-Cookie");
			is = huc.getInputStream();
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest(getReplyString(is, huc.getContentEncoding()));
			} else {
				consumeReply(is);
			}

			// get specified objects such as Desktop
			Desktop desktop = (Desktop) emulator.getRequestAttributes().get("javax.zkoss.zk.ui.desktop");
			// TODO, what if a non-zk(zul) page, throw exception?
			DesktopAgent desktopAgent = new DefaultDesktopAgent(this, desktop);
			desktopAgentList.add(desktopAgent);
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

		for (DesktopAgent d : desktopAgentList) {
			destroy(d);
		}
		desktopAgentList.clear();
	}

	public void destroy(DesktopAgent desktopAgent) {
		postUpdate(desktopAgent.getId(),"rmDesktop",null,null,"i");
	}
	
	public void postUpdate(String desktopId, String command, String targetUUID, Map<String, Object> data,String option) {
		if(desktopId==null){
			throw new IllegalArgumentException("desktop id is null");
		}else if(command == null){
			throw new IllegalArgumentException("command is null");
		}
		
		// prepare au data
		final String dtid = UrlEncoded.encodeString(desktopId);
		final String cmd = command = UrlEncoded.encodeString(command);
		final StringBuilder param = new StringBuilder();
		
		param.append("dtid=").append(dtid).append("&cmd_0=").append(cmd);
		
		if(targetUUID!=null){
			String uuid = UrlEncoded.encodeString(targetUUID);
			param.append("&uuid_0=").append(uuid);
		}
		if(data != null && data.size() > 0){
			String jsonData = UrlEncoded.encodeString(JSONValue.toJSONString(data));
			param.append("&data_0=").append(jsonData);
		}
		if(option!=null && option.length()>0){
			option = UrlEncoded.encodeString(option);
			param.append("&opt_0=").append(option);
		}

		final String content = param.toString();
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(targetUUID + " perform AU: " + UrlEncoded.decodeString(content, 0, content.length(), "utf-8"));
		}

		OutputStream os = null;
		InputStream is = null;
		try {
			// create http request and perform it
			HttpURLConnection c = getConnection("/zkau", "POST");
			c.setDoOutput(true);
			c.setDoInput(true);
			for (String cookie : cookies) {
				// handle cookie for session
				c.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			}
			c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			c.connect();
			os = c.getOutputStream();
			os.write(content.getBytes("utf-8"));
			close(os);
			// TODO, read response, handle redirect.
			// read response
			is = c.getInputStream();
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest(getReplyString(is, c.getContentEncoding()));
			} else {
				consumeReply(is);
			}
		} catch (Exception e) {
			throw new ZatsException("", e);
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
			huc.addRequestProperty("Host", emulator.getHost() + ":" + emulator.getPort());
			huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
			return huc;
		} catch (Exception e) {
			throw new ZatsException("", e);
		}
	}

	private void close(Closeable c) {
		try {
			c.close();
		} catch (Throwable e) {
		}
	}

	private void consumeReply(InputStream is) {
		try {
			is = new BufferedInputStream(is);
			while (is.read() >= 0) {
			}
		} catch (Throwable e) {
			logger.log(Level.WARNING, "", e);
		} finally {
			close(is);
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
}
