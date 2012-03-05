package org.zkoss.zats.core.component.operation;

import org.zkoss.zats.core.component.ComponentNode;

public interface Focusable extends Operation
{
	ComponentNode focus();

	ComponentNode blur();
}
