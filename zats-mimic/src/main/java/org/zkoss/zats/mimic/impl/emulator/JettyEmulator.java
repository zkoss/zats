/* JettyEmulator.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.emulator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * The emulator implementation in Jetty Server.
 * @author pao
 */
public class JettyEmulator implements Emulator {
	private static Logger logger = Logger.getLogger(JettyEmulator.class.getName());
	private ReentrantLock lock;

	private Server server;
	private int port;
	private String address;
	private String contextPath;

	private ServletContext context;
	private String sessionId;
	private Map<String, Object> sessionAttributes;
	private Map<String, Object> requestAttributes;
	private Map<String, String[]> requestParameters;

	/** temp directory for jetty server */
	private File tmpDir;

	public JettyEmulator(Resource contentRoot, String descriptor, String contextPath) {
		this(new Resource[] { contentRoot }, descriptor, contextPath);
	}

	/**
	 * new a jetty emulator
	 * @param contentRoots the content roots.
	 * @param descriptor specify the web.xml, if null then use /WEB-INF/web.xml that in contentRoots
	 * @param contextPath specify the context, if null then use "/"
	 */
	public JettyEmulator(Resource[] contentRoots, String descriptor, String contextPath) {
		if (contentRoots == null || contentRoots.length <= 0)
			throw new IllegalArgumentException("contentRoots can't be null.");
		lock = new ReentrantLock(true);
		requestAttributes = new HashMap<String, Object>();
		requestParameters = new HashMap<String, String[]>();
		this.contextPath = contextPath == null ? "/" : contextPath;
		try {
			// create server
			server = new Server(new InetSocketAddress(getHost(), 0));
			final WebAppContext contextHandler = new WebAppContext();
			ResourceCollection resourceCollection = new ResourceCollection(contentRoots);

			contextHandler.setBaseResource(resourceCollection);
			if (descriptor != null) {
				contextHandler.setDescriptor(descriptor);
			}
			contextHandler.setContextPath(this.contextPath);

			contextHandler.setParentLoaderPriority(true);
			// fix issue: the jetty temp. directory is always the same according the configuration of emulator
			// specify different temp. directory to solve this issue.
			File tmpFile = File.createTempFile("jetty.", ".tmp");
			if (!tmpFile.delete())
				tmpFile.deleteOnExit();
			tmpDir = new File(tmpFile.getParent(), tmpFile.getName() + ".dir");
			tmpDir.mkdirs();
			if (tmpDir.exists())
				contextHandler.setTempDirectory(tmpDir);
			else
				tmpDir = null;
			
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
		// shutdown server
		try {
			server.stop();
			server.destroy();
		} catch (Throwable e) {
			// do nothing
		} finally {
			server = null;
		}
		
		// clean resources
		if (tmpDir.getName().startsWith("jetty.")) { // fuse 1
			List<File> resources = depthFirstSearch(tmpDir);
			if (resources.size() < 10) { // fuse 2
				String tmpPath = tmpDir.getAbsolutePath();
				for (File r : resources) {
					String targetPath = r.getAbsolutePath();
					if (targetPath.startsWith(tmpPath)) // fuse 3
					{
						r.delete();
						if (logger.isLoggable(Level.FINE))
							logger.fine("delete resource: " + targetPath);
					}
				}
			}
		}
	}
	
	/**
	 * search sub-files from specify directory with depth first order. 
	 * @param dir search root directory
	 * @return file list with depth first order. return null if source directory is null or not existed.
	 */
	private List<File> depthFirstSearch(File dir) {
		if (dir == null || !dir.exists())
			return null;
		LinkedList<File> targetSquence = new LinkedList<File>();
		LinkedList<File> stack = new LinkedList<File>();
		stack.add(dir);
		while (!stack.isEmpty()) {
			File item = stack.removeFirst();
			if (item.isDirectory()) {
				for (File child : item.listFiles())
					stack.addFirst(child);
			}
			targetSquence.addFirst(item);
		}
		return targetSquence;
	}

	public String getHost() {
		return "127.0.0.1";
	}

	public int getPort() {
		return port;
	}
	
	public String getContextPath(){
		return contextPath;
	}

	public String getAddress() {
		if (address == null){
			String cp = getContextPath();
			if(cp.endsWith("/")){//
				cp = cp.substring(0,cp.length()-1);
			}
			address = MessageFormat.format("http://{0}:{1,number,#}{2}", getHost(), getPort(),cp);
		}
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
