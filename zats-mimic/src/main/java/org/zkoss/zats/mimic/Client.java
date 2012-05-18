/* Client.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.Map;


/**
 * Represent a client that can connect to zul files. It plays a role like a browser but without rendering anything.
 * @author Hawk
 * @author Dennis
 *
 */
public interface Client {
	
	/**
	 * connect to a zul file, you have to provide the path that relative to the resource root folder
	 * 
	 * @see ZatsEnvironment#init(String)
	 */
	DesktopAgent connect(String zulPath);

	
	/**
	 * destroy this client, it will also destory all un-destroyed desktops that is created by this client
	 */
	void destroy();
	
	/**
	 * set cookie for the client, the cookie will be sent at every HTTP request.
	 * The cookie name can't start with '$'. Please refer to section 3.2.2 of RFC 2965.
	 * If the name existed, the origin value will be replaced.
	 * @param name The name of the cookie, it should not be null or empty string.  
	 * @param value The value of the cookie. If the value is null, it will erase this cookie.
	 */
	void setCookie(String name, String value);

	/**
	 * get value of cookie with specify name. 
	 * @param name The name of the cookie, it should not be null or empty string.
	 * @return the cookie value if cookie is existed or null otherwise.
	 */
	String getCookie(String name);

	/**
	 * get all cookies in an unmodifiable map.
	 * @return the map contained cookies.
	 */
	Map<String, String> getCookies();
}
