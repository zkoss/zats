package org.zkoss.zats.example.login;

import java.util.HashMap;

public class AuthenticationService {

	static HashMap<String, String> userData = new HashMap<String, String>(); 
	static{
		userData.put("hawk", "1234");
	}
		
	public boolean authenticate(String account, String password){
		boolean pass = false;
		if (password.equals(userData.get(account))){
			pass = true;
		}
		
		return pass;
		
	}
}
