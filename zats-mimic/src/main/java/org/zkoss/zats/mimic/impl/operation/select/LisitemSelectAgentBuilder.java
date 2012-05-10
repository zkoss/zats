/* LisitemSelectAgentBuilder.java

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
import org.zkoss.zul.Listitem;

/**
 * 
 * @author dennis
 *@author pao
 */
public class LisitemSelectAgentBuilder extends AbstractSelectAgentBuilder {
	public SelectAgent getOperation(ComponentAgent target) {
		return new AbstractSelectAgentImpl(target) {
			@Override
			protected Component getEventTarget() {
				return target.as(Listitem.class).getListbox();
			}

			@Override
			protected void contributeExtraInfo(Map<String, Object> data) {
				data.put("clearFirst", target.as(Listitem.class).getListbox().isMultiple());
			}
		};
	}

	public Class<SelectAgent> getOperationClass() {
		return SelectAgent.class;
	}
}