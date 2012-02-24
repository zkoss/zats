package org.zkoss.zk.zats.env.emulator;

import java.io.Closeable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Emulator extends Closeable
{
	/**
	 * get the host address the emulator bound.
	 * normally, it should be "127.0.0.1".
	 * @return host address
	 */
	String getHost();

	/**
	 * get the port number the emulator bound.
	 * @return port number
	 */
	int getPort();

	/**
	 * get the URL of this address the emulator bound.
	 * @return URL (eg. http://127.0.0.1:12345)
	 */
	String getAddress();

	/**
	 * get the reference of servlet context in the emulator.
	 * @return servlet context
	 */
	ServletContext getServletContext();

	/**
	 * get the last servlet request object in the emulator.
	 * @return servlet request or null if it was not connected yet.
	 */
	HttpServletRequest getLastServletRequest();

	/**
	 * get the last servlet response object in the emulator.
	 * @return servlet response or null if it was not connected yet.
	 */
	HttpServletResponse getLastServletResponse();
}
