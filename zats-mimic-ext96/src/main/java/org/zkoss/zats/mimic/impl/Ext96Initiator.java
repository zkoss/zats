/* Ext96Initiator.java

	Purpose:
		
	Description:
		
	History:
		Fri Jun 4 12:46:50 CST 2021, Created by jameschu

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericOpenAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.DateTypeAgentBuilderZK96;
import org.zkoss.zats.mimic.impl.operation.input.TimeTypeAgentBuilderZK96;
import org.zkoss.zats.mimic.impl.operation.select.SelectboxSelectByIndexAgentBuilder;
import org.zkoss.zats.mimic.impl.response.EchoEventHandler;
import org.zkoss.zats.mimic.impl.response.EchoEventHandlerExt96;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;
import org.zkoss.zul.Combobutton;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;

/**
 * @author dennis
 * @author jameschu
 */
public class Ext96Initiator implements WebAppInit{

	public void init(WebApp wapp) throws Exception {
		
		//testcases and mimic server is in the same vm. 
		//so it is ok to register builder by webapp init
		
		// operation
		OperationAgentManager operationAgentManager = OperationAgentManager.getInstance();
		
		operationAgentManager.registerBuilder("9.6.0", "*", Toolbarbutton.class,
				new GenericCheckAgentBuilder()); // toolbarbutton on check in zk9.6
		operationAgentManager.registerBuilder("9.6.0", "*", Datebox.class,
				new DateTypeAgentBuilderZK96()); // date format changed in zk9.6
		operationAgentManager.registerBuilder("9.6.0", "*", Timebox.class,
				new TimeTypeAgentBuilderZK96()); // date format changed in zk9.6
		operationAgentManager.registerBuilder("9.6.0", "*", Combobutton.class,
				new GenericOpenAgentBuilder()); // combobutton introduced since zk9.6
		operationAgentManager.registerBuilder("9.6.0", "*", Selectbox.class,
				new SelectboxSelectByIndexAgentBuilder()); // selectbox introduced since zk9.6
		
		String navitemClassName = "org.zkoss.zkmax.zul.Navitem";
		if(Util.hasClass(navitemClassName)){
			operationAgentManager.registerBuilder("9.6.0", "*", navitemClassName,
					"org.zkoss.zats.mimic.impl.operation.select.NavitemSelectAgentBuilder"); // navbar/navitem introduced in ZK9.6 (zkmax only)
		}
		// event data
		// resolvers
		// resolve view model
		if (Util.hasClass("org.zkoss.bind.Binder")) {
			ValueResolverManager.getInstance().registerResolver("9.6.0", "*", "bind",
					"org.zkoss.zats.mimic.impl.BindValueResolver");
		}
		// layout response handlers
		ResponseHandlerManager.getInstance().registerHandler("9.6.0", "*", EchoEventHandler.REGISTER_KEY,
				(LayoutResponseHandler) new EchoEventHandlerExt96());
		// update response handlers
		// ZATS-11: note that, the key can be used for replacing previous one and prevent duplicate handlers
		ResponseHandlerManager.getInstance().registerHandler("9.6.0", "*", EchoEventHandler.REGISTER_KEY,
				(UpdateResponseHandler) new EchoEventHandlerExt96());
	}
}
