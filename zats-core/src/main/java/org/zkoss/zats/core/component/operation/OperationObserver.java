package org.zkoss.zats.core.component.operation;

import java.util.Map;
import org.zkoss.zk.ui.Component;

public interface OperationObserver
{
	void doPost(Component target, String cmd, Map<String, Object> param);
}
