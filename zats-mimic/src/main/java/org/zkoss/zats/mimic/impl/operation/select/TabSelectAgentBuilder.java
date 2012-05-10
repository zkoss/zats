/* TabSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 9, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation.select;

import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.Component;

/**
 * 
 * @author dennis
 * @author pao
 */
public class TabSelectAgentBuilder extends AbstractSelectAgentBuilder {
	public SelectAgent getOperation(ComponentAgent target) {
		return new AbstractSelectAgentImpl(target) {
			@Override
			protected Component getEventTarget() {
				return (Component) target.getDelegatee();
			}

			@Override
			protected void contributeExtraInfo(Map<String, Object> data) {
			}
		};
	}

	public Class<SelectAgent> getOperationClass() {
		return SelectAgent.class;
	}
}