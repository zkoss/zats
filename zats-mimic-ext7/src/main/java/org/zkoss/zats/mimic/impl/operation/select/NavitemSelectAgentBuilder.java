package org.zkoss.zats.mimic.impl.operation.select;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkmax.zul.Navitem;

public class NavitemSelectAgentBuilder extends AbstractSelectAgentBuilder {

	public SelectAgent getOperation(ComponentAgent agent) {
		return new AbstractSelectAgentImpl(agent) {
			@Override
			protected Component getEventTarget() {
				return target.as(Navitem.class).getNavbar();
			}

			@Override
			protected void contributeExtraInfo(Map<String, Object> data) {
				//no extra info
			}
		};
	}

}
