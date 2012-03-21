/* GenericFocusAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;

public class GenericFocusAgentBuilder implements OperationAgentBuilder<FocusAgent> {
	public FocusAgent getOperation(final ComponentAgent target) {
		return new FocusAgentImpl(target);
	}
	class FocusAgentImpl extends AgentDelegator implements FocusAgent{
		public FocusAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void focus() {
			AuUtility.postOnFocus(target);
		}

		public void blur() {
			AuUtility.postOnBlur(target);
		}
	}
}
