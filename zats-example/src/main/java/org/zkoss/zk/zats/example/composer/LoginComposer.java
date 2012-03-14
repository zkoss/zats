package org.zkoss.zk.zats.example.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.zats.example.service.AuthenticationService;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class LoginComposer extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;

	AuthenticationService authenticationService = new AuthenticationService();
	
	Textbox account;
	Textbox password;
	Label msg;
	
	public void onClick$login() {
		boolean pass = false;
		if (account.getValue() != null && password.getValue()!=null){
			pass = authenticationService.authenticate(account.getValue(), password.getValue());
		}
		if (pass){
			sessionScope.put("auth", account.getValue());
			Executions.getCurrent().sendRedirect("main.zul");
		}else{
			msg.setValue("Login Failed");
		}
	}
	
	public void onClick$logout(){
		sessionScope.remove("auth");
		Executions.getCurrent().sendRedirect("login.zul");
		
	}

}
