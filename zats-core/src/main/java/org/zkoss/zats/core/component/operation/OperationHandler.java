package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.component.ComponentNode;

public interface OperationHandler<T extends Operation> {
	T getOperation(Conversation conversation, ComponentNode target);
}
