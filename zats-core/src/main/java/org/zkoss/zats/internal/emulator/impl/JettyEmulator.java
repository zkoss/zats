package org.zkoss.zats.internal.emulator.impl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.zkoss.zats.internal.emulator.Emulator;
import org.zkoss.zats.internal.emulator.EmulatorException;

public class JettyEmulator extends AbstractHandler implements Emulator
{
	private int port;
	private Server server;
	private ServletContext context;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public JettyEmulator(String contentPath, String descriptor)
	{
		if(contentPath == null)
			throw new IllegalArgumentException("contentPath can't be null.");
		if(descriptor == null)
			throw new IllegalArgumentException("descriptor can't be null.");
		try
		{
			// create server
			final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
			server = new Server(new InetSocketAddress(getAddress(), 0));
			WebAppContext contextHandler = new WebAppContext();
			contextHandler.setResourceBase(contentPath);
			contextHandler.setDescriptor(descriptor);
			contextHandler.setContextPath("/");
			contextHandler.setParentLoaderPriority(true);
			contextHandler.addLifeCycleListener((Listener)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{ Listener.class }, new InvocationHandler()
			{
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
				{
					if("lifeCycleStarted".equals(method.getName()))
						queue.add(method.getName());
					return null;
				}
			}));

			// observe request and get related ref.
			HandlerCollection handlers = new HandlerCollection();
			handlers.addHandler(contextHandler);
			handlers.addHandler(this);
			server.setHandler(handlers);
			// start server and fetch the real port number and servlet context
			server.start();
			context = contextHandler.getServletHandler().getServletContext();
			for(Connector c : server.getConnectors())
			{
				if(c.getLocalPort() > 0)
				{
					port = c.getLocalPort();
					break;
				}
			}
			// wait for start
			queue.take();
		}
		catch(Exception e)
		{
			throw new EmulatorException(e);
		}
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		this.request = request;
		this.response = response;
	}

	public String getHost()
	{
		return MessageFormat.format("http://127.0.0.1:{0,number,#}/", port);
	}

	public int getPort()
	{
		return port;
	}

	public String getAddress()
	{
		return "127.0.0.1";
	}

	public ServletContext getServletContext()
	{
		return context;
	}

	public HttpServletRequest getLastServletRequest()
	{
		return request;
	}

	public HttpServletResponse getLastServletResponse()
	{
		return response;
	}

	public synchronized void close() throws IOException
	{
		if(server == null)
			return;
		if(!server.isStopping() && !server.isStopped())
			server.destroy();
		server = null;
	}

}
