package org.zkoss.zats.core.impl;

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
import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.ConversationException;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.core.component.impl.DefaultDesktopNode;
import org.zkoss.zats.internal.emulator.Emulator;
import org.zkoss.zats.internal.emulator.EmulatorBuilder;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.sys.DesktopCtrl;

public class EmulatorConversation implements Conversation
{
	private Emulator emulator;
	private Logger logger;
	private File web;
	private DesktopNode desktopNode;
	private List<String> cookies;

	public EmulatorConversation()
	{
		logger = Logger.getLogger(EmulatorConversation.class.getName());
		cookies = new LinkedList<String>();
		// prepare environment
		String tmpDir = System.getProperty("java.io.tmpdir", ".");
		File webinf = new File(tmpDir, System.currentTimeMillis() + "/WEB-INF");
		if(!webinf.mkdirs())
			throw new ConversationException("can't create temp directory");
		web = webinf.getParentFile();
		copy(EmulatorConversation.class.getResourceAsStream("WEB-INF/zk.xml"), new File(web, "WEB-INF/zk.xml"));
	}

	public void start(String resourceRoot)
	{
		// create emulator
		emulator = new EmulatorBuilder(web).addResource(resourceRoot).descriptor(EmulatorConversation.class.getResource("WEB-INF/web.xml")).create();
	}

	public synchronized void stop()
	{
		try
		{
			if(emulator == null)
				emulator.close();
		}
		finally
		{
			emulator = null;
		}
	}

	public void open(String zulPath)
	{
		InputStream is = null;
		try
		{
			// load zul page
			HttpURLConnection huc = getConnection(zulPath, "GET");
			huc.connect();
			cookies = huc.getHeaderFields().get("Set-Cookie");
			is = huc.getInputStream();
			if(logger.isLoggable(Level.INFO))
				logger.info(getReplyString(is, huc.getContentEncoding()));
			// get specified objects such as Desktop
			Desktop desktop = (Desktop)emulator.getRequestAttributes().get("javax.zkoss.zk.ui.desktop");
			desktopNode = new DefaultDesktopNode(this, desktop);
		}
		catch(Exception e)
		{
			throw new ConversationException("", e);
		}
		finally
		{
			close(is);
		}
	}

	public void clean()
	{
		// clean desktop
		try
		{
			if(desktopNode != null)
			{
				Desktop desktop = desktopNode.cast();
				if(desktop instanceof DesktopCtrl)
					((DesktopCtrl)desktop).destroy();
			}
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING, "", e);
		}
		finally
		{
			desktopNode = null;
			cookies = new LinkedList<String>();
		}
	}

	public DesktopNode getDesktop()
	{
		return desktopNode;
	}

	public HttpSession getSession()
	{
		if(desktopNode == null)
			return null;
		return (HttpSession)desktopNode.cast().getSession().getNativeSession();
	}

	public void postUpdate(ComponentNode target, String cmd, Map<String, Object> data)
	{
		// prepare au data
		String dtid = UrlEncoded.encodeString(desktopNode.getId());
		cmd = UrlEncoded.encodeString(cmd);
		String uuid = UrlEncoded.encodeString(target.getUuid());
		String param;
		if(data != null && data.size() > 0)
		{
			String jsonData = UrlEncoded.encodeString(JSONValue.toJSONString(data));
			param = MessageFormat.format("dtid={0}&cmd_0={1}&uuid_0={2}&data_0={3}", dtid, cmd, uuid, jsonData);
		}
		else
			param = MessageFormat.format("dtid={0}&cmd_0={1}&uuid_0={2}", dtid, cmd, uuid);

		OutputStream os = null;
		InputStream is = null;
		try
		{
			// create http request and perform it
			HttpURLConnection c = getConnection("/zkau", "POST");
			c.setDoOutput(true);
			c.setDoInput(true);
			for(String cookie : cookies)
				c.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			c.connect();
			os = c.getOutputStream();
			os.write(param.getBytes("utf-8"));
			close(os);
			// read response
			is = c.getInputStream();
			if(logger.isLoggable(Level.INFO))
				logger.info(getReplyString(is, c.getContentEncoding()));
		}
		catch(Exception e)
		{
			throw new ConversationException("", e);
		}
		finally
		{
			close(os);
			close(is);
		}
	}

	private HttpURLConnection getConnection(String path, String method)
	{
		try
		{
			URL url = new URL(emulator.getAddress() + path);
			HttpURLConnection huc = (HttpURLConnection)url.openConnection();
			huc.setRequestMethod(method);
			huc.setUseCaches(false);
			huc.addRequestProperty("Host", emulator.getHost() + ":" + emulator.getPort());
			huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
			return huc;
		}
		catch(Exception e)
		{
			throw new ConversationException("", e);
		}
	}

	private void copy(InputStream src, File dest)
	{
		OutputStream os = null;
		try
		{
			os = new FileOutputStream(dest);
			byte[] buf = new byte[65536];
			int len;
			while(true)
			{
				len = src.read(buf);
				if(len < 0)
					break;
				os.write(buf, 0, len);
			}
		}
		catch(Exception e)
		{
			throw new ConversationException("fail to copy file", e);
		}
		finally
		{
			close(src);
			close(os);
		}
	}

	private void close(Closeable c)
	{
		try
		{
			c.close();
		}
		catch(Throwable e)
		{
		}
	}

	private String getReplyString(InputStream is, String encoding)
	{
		String reply = null;
		Reader r = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			r = new BufferedReader(new InputStreamReader(is, encoding != null ? encoding : "ISO-8859-1"));
			while(true)
			{
				int c = r.read();
				if(c < 0)
					break;
				sb.append((char)c);
			}
			reply = sb.toString();
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING, "", e);
		}
		finally
		{
			close(r);
		}
		return reply;
	}
}
