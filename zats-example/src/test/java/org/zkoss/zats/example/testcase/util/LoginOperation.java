package org.zkoss.zats.example.testcase.util;


import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;

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
		accountBox.as(TypeAgent.class).type(account);
		passwordBox.as(TypeAgent.class).type(password);
		login.as(ClickAgent.class).click();
		
		HttpSession session = Conversations.getSession();
		if (session.getAttribute("user")==null){
			return false;
		}else{
			return true;
		}
	}
	
}
