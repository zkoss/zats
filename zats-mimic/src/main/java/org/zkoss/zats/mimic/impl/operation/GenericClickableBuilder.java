/* GenericClickableBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Clickable;

public class GenericClickableBuilder implements OperationBuilder<Clickable> {
	public Clickable getOperation(final ComponentNode target) {
		return new Clickable() {
			public Clickable click() {
				PostAgent.doClick(target);
				return this;
			}

			public Clickable doubleClick() {
				PostAgent.doDoubleClick(target);
				return this;
			}

			public Clickable rightClick() {
				PostAgent.doRightClick(target);
				return this;
			}
		};
	}
}
