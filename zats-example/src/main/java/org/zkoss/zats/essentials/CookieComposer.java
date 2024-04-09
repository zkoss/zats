/* CookieComposer.java

	Purpose:
		
	Description:
		
	History:
		2013/4/24 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.essentials;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

/**
 * @author Hawk
 *
 */
public class CookieComposer extends SelectorComposer<Component>{

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setCookie("foo", "bar");
	}

	@Listen("onClick=#change")
	public void change() {
		setCookie("foo", "hello");
	}

	public void setCookie(String name, String value) {
		HttpServletResponse resp = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
		resp.addCookie(new Cookie(name, value));
	}
}
