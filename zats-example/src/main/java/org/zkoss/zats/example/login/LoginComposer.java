package org.zkoss.zats.example.login;

import java.util.logging.Logger;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class LoginComposer extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;

	AuthenticationService authenticationService = new AuthenticationService();
	
	Textbox account;
	Textbox password;
	Label msg;
	
	private Logger logger;
	public LoginComposer() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(this.getClass().getName());
//		logger.setLevel(Level.ALL);
		
	}
	
	public void onClick$login() {
		boolean pass = false;
		if (account.getValue() != null && password.getValue()!=null){
			pass = authenticationService.authenticate(account.getValue(), password.getValue());
		}
		if (pass){
			sessionScope.put("user", account.getValue());
			logger.info(desktop.getId()+", "+desktop.getRequestPath());
			Executions.getCurrent().sendRedirect("main.zul");
		}else{
			msg.setValue("Login Failed");
		}
	}
	
	public void onClick$logout(){
		logger.info(desktop.getId()+", "+desktop.getRequestPath());
		sessionScope.remove("user");
		Executions.getCurrent().sendRedirect("login.zul");
		
	}

}
