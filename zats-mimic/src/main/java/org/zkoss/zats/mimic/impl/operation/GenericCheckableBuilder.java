/* GenericCheckableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Checkable;

public class GenericCheckableBuilder implements OperationBuilder<Checkable> {

	public Checkable getOperation(final ComponentNode target) {
		return new Checkable() {
			public Checkable check(boolean checked) {
				OperationUtil.doCheck(target, checked);
				return this;
			}
		};
	}

}
