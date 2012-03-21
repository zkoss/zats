/* GenericFocusableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.FocusAgent;

public class GenericFocusAgentBuilder implements OperationAgentBuilder<FocusAgent> {
	public FocusAgent getOperation(final ComponentAgent target) {
		return new FocusAgent() {
			public FocusAgent focus() {
				AuUtility.postFocus(target);
				return this;
			}

			public FocusAgent blur() {
				AuUtility.postBlur(target);
				return this;
			}
		};
	}

}
