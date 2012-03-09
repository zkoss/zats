package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.component.ComponentNode;

public interface OperationBuilder<T extends Operation>
{
	T getOperation(ComponentNode target);
}
