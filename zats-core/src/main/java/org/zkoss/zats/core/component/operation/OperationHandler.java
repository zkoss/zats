package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.component.Node;

public interface OperationHandler
{
	/**
	 * @param operation
	 * @param target
	 * @return
	 */
	<T extends Operation> String handle(Class<T> operation, Node target);
}
