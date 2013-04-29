/* Initial.java

	Purpose:
		
	Description:
		
	History:
		2012/3/23 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.GenericOpenAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.DateTypeAgentBuilderZK6;
import org.zkoss.zats.mimic.impl.operation.input.TimeTypeAgentBuilderZK6;
import org.zkoss.zats.mimic.impl.operation.select.SelectboxSelectByIndexAgentBuilder;
import org.zkoss.zats.mimic.impl.response.EchoEventHandler;
import org.zkoss.zats.mimic.impl.response.EchoEventHandlerExt6;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;
import org.zkoss.zul.Combobutton;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;

/**
 * @author dennis
 *
 */
public class Ext6Initiator implements WebAppInit{

	public void init(WebApp wapp) throws Exception {
		
		//testcases and mimic server is in the same vm. 
		//so it is ok to register builder by webapp init
		
		// operation
		OperationAgentManager.getInstance().registerBuilder("6.0.0", "*", Toolbarbutton.class,
				new GenericCheckAgentBuilder()); // toolbarbutton on check in zk6 only 
		OperationAgentManager.getInstance().registerBuilder("6.0.0", "*", Datebox.class,
				new DateTypeAgentBuilderZK6()); // date format changed in zk6
		OperationAgentManager.getInstance().registerBuilder("6.0.0", "*", Timebox.class,
				new TimeTypeAgentBuilderZK6()); // date format changed in zk6
		OperationAgentManager.getInstance().registerBuilder("6.0.0", "*", Combobutton.class,
				new GenericOpenAgentBuilder()); // combobutton introduced since zk6
		OperationAgentManager.getInstance().registerBuilder("6.0.0", "*", Selectbox.class,
				new SelectboxSelectByIndexAgentBuilder()); // selectbox introduced since zk6
		
		// event data
		
		// resolvers
		// resolve view model
		if (Util.hasClass("org.zkoss.bind.Binder")) {
			ValueResolverManager.getInstance().registerResolver("6.0.0", "*", "bind",
					"org.zkoss.zats.mimic.impl.BindValueResolver");
		}
		
		// layout response handlers
		ResponseHandlerManager.getInstance().registerHandler("6.0.0", "*", EchoEventHandler.REGISTER_KEY,
				(LayoutResponseHandler) new EchoEventHandlerExt6());
		
		// update response handlers
		// ZATS-11: note that, the key can be used for replacing previous one and prevent duplicate handlers
		ResponseHandlerManager.getInstance().registerHandler("6.0.0", "*", EchoEventHandler.REGISTER_KEY,
				(UpdateResponseHandler) new EchoEventHandlerExt6());
	}

}
