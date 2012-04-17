/* JettyEmulator.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.emulator;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * The emulator implementation in Jetty Server.
 * @author pao
 */
public class JettyEmulator implements Emulator {
	private ReentrantLock lock;

	private Server server;
	private int port;
	private String address;

	private ServletContext context;
	private String sessionId;
	private Map<String, Object> sessionAttributes;
	private Map<String, Object> requestAttributes;
	private Map<String, String[]> requestParameters;

	public JettyEmulator(String contentRoot, String descriptor, String contextPath) {
		this(new String[] { contentRoot }, descriptor, contextPath);
	}

	/**
	 * new a jetty emulator
	 * @param contentRoots the content roots.
	 * @param descriptor specify the web.xml, if null then use /WEB-INF/web.xml that in contentRoots
	 * @param contextPath specify the context, if null then use "/"
	 */
	public JettyEmulator(String[] contentRoots, String descriptor, String contextPath) {
		if (contentRoots == null || contentRoots.length <= 0)
			throw new IllegalArgumentException("contentRoots can't be null.");

		lock = new ReentrantLock(true);
		requestAttributes = new HashMap<String, Object>();
		requestParameters = new HashMap<String, String[]>();

		try {
			// create server
			server = new Server(new InetSocketAddress(getHost(), 0));
			final WebAppContext contextHandler = new WebAppContext();
			ResourceCollection resourceCollection = new ResourceCollection(contentRoots);

			contextHandler.setBaseResource(resourceCollection);
			if (descriptor != null) {
				contextHandler.setDescriptor(descriptor);
			}
			contextHandler.setContextPath(contextPath == null ? "/" : contextPath);

			contextHandler.setParentLoaderPriority(true);
			// observe request and get related ref.
			HandlerCollection handlers = new HandlerCollection();
			handlers.addHandler(new BeforeHandler());
			handlers.addHandler(contextHandler);
			handlers.addHandler(new AfterHandler());
			server.setHandler(handlers);

			// synchronize initial step
			final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1, true);
			contextHandler.addLifeCycleListener((Listener) Proxy.newProxyInstance(Thread.currentThread()
					.getContextClassLoader(), new Class<?>[] { Listener.class }, new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if ("lifeCycleStarted".equals(method.getName()))
						queue.add(method.getName());
					return null;
				}
			}));
			// start server and wait for started
			server.start();
			queue.take();

			// fetch the real port number
			for (Connector c : server.getConnectors()) {
				if (c.getLocalPort() > 0) {
					port = c.getLocalPort();
					break;
				}
			}
			// get servlet context and synchronize access
			context = getWrappedContext(contextHandler.getServletHandler().getServletContext());
		} catch (Exception e) {
			throw new EmulatorException("", e);
		}
	}

	public void close() {
		if (server == null)
			return;
		try {
			server.destroy();
		} catch (Throwable e) {
			// do nothing
		} finally {
			server = null;
		}
	}

	public String getHost() {
		return "127.0.0.1";
	}

	public int getPort() {
		return port;
	}

	public String getAddress() {
		if (address == null)
			address = MessageFormat.format("http://{0}:{1,number,#}", getHost(), getPort());
		return address;
	}

	public ServletContext getServletContext() {
		return context;
	}

	public Map<String, Object> getRequestAttributes() {
		lock.lock();
		try {
			return requestAttributes;
		} finally {
			lock.unlock();
		}
	}

	public Map<String, String[]> getRequestParameters() {
		lock.lock();
		try {
			return requestParameters;
		} finally {
			lock.unlock();
		}
	}

	public Map<String, Object> getSessionAttributes() {
		lock.lock();
		try {
			return sessionAttributes;
		} finally {
			lock.unlock();
		}
	}

	public String getSessionId() {
		lock.lock();
		try {
			return sessionId;
		} finally {
			lock.unlock();
		}
	}

	private ServletContext getWrappedContext(final ServletContext delegate) {
		return (ServletContext) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { ServletContext.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (!method.getName().startsWith("getAttribute"))
							return method.invoke(delegate, args);
						lock.lock();
						try {
							return method.invoke(delegate, args);
						} finally {
							lock.unlock();
						}
					}
				});
	}

	/**
	 * The handler before original handler. lock attributes access.
	 * 
	 * @author pao
	 */
	private class BeforeHandler extends AbstractHandler {
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			// lock access of all attributes
			lock.lock();
		}
	}

	/**
	 * The handler after original handler. Fetch attributes and release access
	 * lock.
	 * 
	 * @author pao
	 */
	private class AfterHandler extends AbstractHandler {
		@SuppressWarnings("unchecked")
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			try {
				// copy attributes for test case
				requestParameters = new HashMap<String, String[]>(request.getParameterMap());
				HttpSession session = request.getSession(false);
				sessionId = session != null ? session.getId() : null;
				sessionAttributes = new HashMap<String, Object>();
				if (session != null) {
					Enumeration<String> names = session.getAttributeNames();
					while (names.hasMoreElements()) {
						String name = names.nextElement();
						sessionAttributes.put(name, session.getAttribute(name));
					}
				}
				requestAttributes = new HashMap<String, Object>();
				Enumeration<String> names = request.getAttributeNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					requestAttributes.put(name, request.getAttribute(name));
				}
			} finally {
				lock.unlock();
			}
		}
	}
}
