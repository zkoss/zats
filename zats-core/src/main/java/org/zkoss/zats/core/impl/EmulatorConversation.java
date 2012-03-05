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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.ConversationException;
import org.zkoss.zats.core.component.DesktopNode;
import org.zkoss.zats.internal.emulator.Emulator;
import org.zkoss.zats.internal.emulator.EmulatorBuilder;

public class EmulatorConversation implements Conversation
{
	private Emulator emulator;
	private Logger logger;

	public EmulatorConversation()
	{
		logger = Logger.getLogger(EmulatorConversation.class.getName());
		logger.setLevel(Level.ALL);
		logger.finest("cool");
		System.out.println(logger.getName());
		// prepare environment
		String tmpDir = System.getProperty("java.io.tmpdir", ".");
		File webinf = new File(tmpDir, System.currentTimeMillis() + "/WEB-INF");
		if(!webinf.mkdirs())
			throw new ConversationException("can't create temp directory");
		File web = webinf.getParentFile();
		// copy("WEB-INF/web.xml", web);
		copy("WEB-INF/zk.xml", web);
		// create emulator
		emulator = new EmulatorBuilder(web).descriptor(EmulatorConversation.class.getResource("WEB-INF/web.xml")).create();
	}

	private void copy(String file, File root)
	{
		InputStream is = null;
		OutputStream os = null;
		try
		{
			is = EmulatorConversation.class.getResourceAsStream(file);
			os = new FileOutputStream(new File(root, file));
			byte[] buf = new byte[65536];
			int len;
			while(true)
			{
				len = is.read(buf);
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
			close(is);
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

	public synchronized void destory()
	{
		if(emulator == null)
			return;
		try
		{
			emulator.close();
		}
		catch(Exception e)
		{
			throw new ConversationException("", e);
		}
		finally
		{
			emulator = null;
		}
	}

	public void open(String zulPath)
	{
		HttpURLConnection huc = null;
		try
		{
			URL url = new URL(emulator.getAddress() + zulPath.trim());
			huc = (HttpURLConnection)url.openConnection();
			huc.setRequestMethod("GET");
			huc.addRequestProperty("Host", emulator.getHost() + ":" + emulator.getPort());
			huc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			huc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			huc.addRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
			huc.connect();
			InputStream is = huc.getInputStream();
			if(logger.isLoggable(Level.ALL))
				logger.finest(getReplyString(is, huc.getContentEncoding()));
			close(is);
		}
		catch(Exception e)
		{
			new ConversationException("", e);
		}
		finally
		{
			if(huc != null)
				huc.disconnect();
		}
	}

	public DesktopNode getDesktop()
	{
		return null;
	}

	public HttpSession getSession()
	{
		return null;
	}

}
