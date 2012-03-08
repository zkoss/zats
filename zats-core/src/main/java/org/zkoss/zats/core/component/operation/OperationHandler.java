package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.Conversation;
import org.zkoss.zats.core.component.ComponentNode;

public interface OperationHandler {

	Class<Operation>[] getSupportedOperations();

	<T extends Operation> T getOperation(Class<T> c, Conversation conversation, ComponentNode target);
}
