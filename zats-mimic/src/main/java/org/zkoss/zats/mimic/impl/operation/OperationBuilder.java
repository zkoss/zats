package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Operation;

public interface OperationBuilder<T extends Operation>
{
	T getOperation(ComponentNode target);
}
