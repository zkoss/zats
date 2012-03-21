/* GenericClickAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;

public class GenericClickAgentBuilder implements OperationAgentBuilder<ClickAgent> {
	public ClickAgent getOperation(final ComponentAgent target) {
		return new ClickAgentImpl(target);
	}
	
	class ClickAgentImpl extends AgentDelegator implements ClickAgent{
		public ClickAgentImpl(ComponentAgent delegatee) {
			super(delegatee);
		}

		public void click() {
			AuUtility.postClick(target);
		}

		public void doubleClick() {
			AuUtility.postDoubleClick(target);
		}

		public void rightClick() {
			AuUtility.postRightClick(target);
		}
	}
}
