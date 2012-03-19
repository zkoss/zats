package org.zkoss.zk.zats.example.util;

import static org.zkoss.zats.mimic.Searcher.find;

import javax.servlet.http.HttpSession;

import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Clickable;
import org.zkoss.zats.mimic.operation.Typeable;

/**
 * This class contains isolated test logic of login which can be reused in different test cases.
 * @author Hawk
 *
 */
public class LoginOperation {

	public static boolean login(String account, String password){
		ComponentNode accountBox = find("#account");
		ComponentNode passwordBox = find("#password");
		ComponentNode login = find("button");
		
		//login failed
		accountBox.as(Typeable.class).type(account);
		passwordBox.as(Typeable.class).type(password);
		login.as(Clickable.class).click();
		
		HttpSession session = Conversations.getSession();
		if (session.getAttribute("user")==null){
			return false;
		}else{
			return true;
		}
	}
	
}
