package org.zkoss.zk.zats.example.util;

import static org.zkoss.zats.mimic.Searcher.find;

import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;

/**
 * This class contains isolated test logic of login which can be reused in different test cases.
 * @author Hawk
 *
 */
public class LoginOperation {

	public static boolean login(String account, String password){
		ComponentAgent accountBox = find("#account");
		ComponentAgent passwordBox = find("#password");
		ComponentAgent login = find("button");
		
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
