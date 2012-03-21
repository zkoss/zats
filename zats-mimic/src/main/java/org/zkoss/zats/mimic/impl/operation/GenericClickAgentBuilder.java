/* GenericClickableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;

public class GenericClickAgentBuilder implements OperationAgentBuilder<ClickAgent> {
	public ClickAgent getOperation(final ComponentAgent target) {
		return new ClickAgent() {
			public ClickAgent click() {
				AuUtility.postClick(target);
				return this;
			}

			public ClickAgent doubleClick() {
				AuUtility.postDoubleClick(target);
				return this;
			}

			public ClickAgent rightClick() {
				AuUtility.postRightClick(target);
				return this;
			}
		};
	}
}
