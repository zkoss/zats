/* ListboxSelectableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zul.Listbox;

public class ListboxSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {
	public SelectAgent getOperation(final ComponentAgent target) {
		return new SelectAgent() {
			public SelectAgent select(int index) {
				// TODO check type of the target
				AuUtility.postSelect(target, target.as(Listbox.class)
						.getItemAtIndex(index).getUuid());
				return this;
			}
		};
	}
}
