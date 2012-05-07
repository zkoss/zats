/* OperationAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.operation.OperationAgent;

public interface OperationAgentBuilder<A extends Agent,O extends OperationAgent> {
	O getOperation(A agent);

	Class<O> getOperationClass();
}
