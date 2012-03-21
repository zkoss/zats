/* GenericCheckAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.CheckAgent;

public class GenericCheckAgentBuilder implements OperationAgentBuilder<CheckAgent> {

	public CheckAgent getOperation(final ComponentAgent target) {
		return new CheckAgentImpl(target);
	}
	
	class CheckAgentImpl extends AgentDelegator implements CheckAgent{
		public CheckAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void check(boolean checked) {
			AuUtility.postOnCheck(target, checked);
		}
	}
}
