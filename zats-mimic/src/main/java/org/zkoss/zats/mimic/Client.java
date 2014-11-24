/* Client.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.util.Map;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;


/**
 * Represent a client that can connect to zul files. It plays a role like a browser but without rendering anything.
 * @author Hawk
 * @author Dennis
 * @author Pao
 * @author jumperchen
 */
public interface Client {
	
	/**
	 * connect to a zul file, you have to provide the path that relative to the resource root folder
	 * @see ZatsEnvironment#init(String)
	 */
	DesktopAgent connect(String zulPath);
	
	/**
	 * connect to a zul file, you have to provide the path that relative to the resource root folder.
	 * You can pass arguments through a map into the specific zul file as including.
	 * @param zulPath the path of zul file. 
	 * @param args the arguments to pass.
	 * @return desktop agent.
	 * @see ZatsEnvironment#init(String)
	 */
	DesktopAgent connectAsIncluded(String zulPath, Map<String, Object> args);
	
	/**
	 * Connect to a zul content or another language from a string.
	 * You can pass arguments through a map into the specific zul content or another
	 * language.
	 * @param content the raw content of the page. It must be a XML and
	 * compliant to the page format (such as ZUL).
	 * @param extension the default extension if the content doesn't specify
	 * an language. In other words, if
	 * the content doesn't specify an language, {@link LanguageDefinition#getByExtension}
	 * is called.
	 * If extension is null and the content doesn't specify a language,
	 * the language called "xul/html" is assumed.
	 * @param parent the parent component, or null if you want it to be
	 * a root component. If parent is null, the page is assumed to be
	 * the current page, which is determined by the execution context.
	 * In other words, the new component will be the root component
	 * of the current page if parent is null.
	 * @param arg a map of parameters that is accessible by the arg variable
	 * in EL, or by {@link Execution#getArg}.
	 * Ignored if null.
	 * @since 1.2.1
	 * @return desktop agent.
	 * @see Executions#createComponentsDirectly(String, String, org.zkoss.zk.ui.Component, Map)
	 */
	DesktopAgent connectWithContent(String content, String ext, ComponentAgent parent, Map<String, Object> args);

	
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
	 * @since 1.1.0
	 */
	void setCookie(String name, String value);

	/**
	 * get value of cookie with specify name. 
	 * @param name The name of the cookie, it should not be null or empty string.
	 * @return the cookie value if cookie is existed or null otherwise.
	 * @since 1.1.0
	 */
	String getCookie(String name);

	/**
	 * get all cookies in an unmodifiable map.
	 * @return the map contained cookies.
	 * @since 1.1.0
	 */
	Map<String, String> getCookies();
	
	
	/**
	 * change the echo event handling mode.
	 * the default mode is EchoEventMode.IMMEDIATE
	 * @see EchoEventMode
	 * @param mode Control echo event handling mode. If null, do nothing.
	 * @since 1.1.0
	 */
	void setEchoEventMode(EchoEventMode mode);
	
	/**
	 * get the current echo event handling mode.
	 * the default mode is EchoEventMode.IMMEDIATE
	 * @see EchoEventMode
	 * @return current echo event handling mode.
	 * @since 1.1.0
	 */
	EchoEventMode getEchoEventMode();
}
