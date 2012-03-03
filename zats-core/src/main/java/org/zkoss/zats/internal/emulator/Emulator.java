package org.zkoss.zats.internal.emulator;

import java.io.Closeable;
import java.util.Map;
import javax.servlet.ServletContext;

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
	 * get the URL address.
	 * @return URL.
	 */
	String getAddress();

	/**
	 * get the reference of servlet context in the emulator.
	 * @return servlet context
	 */
	ServletContext getServletContext();

	/**
	 * get the attributes of last request.
	 * @return attributes.
	 */
	Map<String, Object> getRequestAttributes();

	/**
	 * get the parameters of last request.
	 * @return parameters.
	 */
	Map<String, String[]> getRequestParameters();

	/**
	 * get the session's attributes of last request.
	 * @return attributes.
	 */
	Map<String, Object> getSessionAttributes();

	/**
	 * get the session ID of last request.
	 * @return ID or null if no session created.
	 */
	String getSessionId();
}
