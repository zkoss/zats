package org.zkoss.zats.example.testcase.util;


import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zk.ui.Desktop;

/**
 * This class contains isolated test logic of login which can be reused in different test cases.
 * @author Hawk
 *
 */
public class LoginOperation {

	public static boolean login(DesktopAgent desktop, String account, String password){
		ComponentAgent accountBox = desktop.query("#account");
		ComponentAgent passwordBox = desktop.query("#password");
		ComponentAgent login = desktop.query("button");
		
		//login failed
		accountBox.as(InputAgent.class).type(account);
		passwordBox.as(InputAgent.class).type(password);
		login.as(ClickAgent.class).click();
		
		HttpSession session = (HttpSession)((Desktop)desktop.getDelegatee()).getSession().getNativeSession();
		if (session.getAttribute("user")==null){
			return false;
		}else{
			return true;
		}
	}
	
}
