package org.zkoss.zats.core.component.operation.impl;

import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Operation;

public interface OperationBuilder<T extends Operation>
{
	T getOperation(ComponentNode target);
}
