package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.OperationHandler;
import org.zkoss.zats.core.component.operation.OperationManager;

public class GenericClickableHandler implements OperationHandler<Clickable> {
	/**
	 * @see org.zkoss.zats.core.component.operation.OperationHandler#getOperation(org.zkoss.zats.core.Conversation,
	 *      org.zkoss.zats.core.component.ComponentNode)
	 */
	public Clickable getOperation(final Conversation conversation, final ComponentNode target) {
		return new Clickable() {
			public Clickable click() {
				// TODO
				String uuid = target.getUuid();
				OperationManager.post("onClick", "");
				return this;
			}
		};
	}

}
