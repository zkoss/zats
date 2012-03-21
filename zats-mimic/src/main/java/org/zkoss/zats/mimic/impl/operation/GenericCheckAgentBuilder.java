/* GenericCheckableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.CheckAgent;

public class GenericCheckAgentBuilder implements OperationAgentBuilder<CheckAgent> {

	public CheckAgent getOperation(final ComponentAgent target) {
		return new CheckAgent() {
			public CheckAgent check(boolean checked) {
				AuUtility.postCheck(target, checked);
				return this;
			}
		};
	}

}
