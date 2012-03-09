package org.zkoss.zats.core.component.operation;

import org.zkoss.zk.ui.Component;

public interface OperationHandler<T extends Operation>
{
	T getOperation(Component target);
}
