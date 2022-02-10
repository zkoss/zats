/* Ext96Initiator.java

	Purpose:
		
	Description:
		
	History:
		Fri Jun 4 12:46:50 CST 2021, Created by jameschu

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

/**
 * @author dennis
 * @author jameschu
 */
public class Ext96Initiator implements WebAppInit {

	public void init(WebApp wapp) throws Exception {
		//testcases and mimic server is in the same vm.
		//so it is ok to register builder by webapp init

		// operation
		OperationAgentManager operationAgentManager = OperationAgentManager.getInstance();

		String navitemClassName = "org.zkoss.zkmax.zul.Navitem";
		if (Util.hasClass(navitemClassName)) {
			operationAgentManager.registerBuilder("9.6.0", "*", navitemClassName,
					"org.zkoss.zats.mimic.impl.operation.select.NavitemSelectAgentBuilder"); // navbar/navitem introduced in ZK9.6 (zkmax only)
		}
	}
}
