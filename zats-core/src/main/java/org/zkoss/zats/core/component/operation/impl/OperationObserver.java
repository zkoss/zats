package org.zkoss.zats.core.component.operation.impl;

import java.util.Map;
import org.zkoss.zats.core.component.ComponentNode;

public interface OperationObserver
{
	void doPost(ComponentNode target, String cmd, Map<String, Object> data);
}
