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
		public ClickAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void click() {
			AuUtility.postOnClick(target);
		}

		public void doubleClick() {
			AuUtility.postOnDoubleClick(target);
		}

		public void rightClick() {
			AuUtility.postOnRightClick(target);
		}
	}
}
