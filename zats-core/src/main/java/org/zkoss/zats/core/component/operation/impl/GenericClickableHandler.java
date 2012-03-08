package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.Operation;
import org.zkoss.zats.core.component.operation.OperationHandler;

public class GenericClickableHandler implements OperationHandler, Clickable {

	/**
	 * @see org.zkoss.zats.core.component.operation.Clickable#click()
	 */
	public Clickable click() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.zkoss.zats.core.component.operation.OperationHandler#getSupportedOperations()
	 */

	public Class<Operation>[] getSupportedOperations() {
		return null;
	}

	/**
	 * @see org.zkoss.zats.core.component.operation.OperationHandler#getOperation(java.lang.Class,
	 *      org.zkoss.zats.core.Conversation, org.zkoss.zats.core.component.ComponentNode)
	 */

	public <T extends Operation> T getOperation(Class<T> c, Conversation conversation, ComponentNode target) {
		return (T) this;
	}
}
