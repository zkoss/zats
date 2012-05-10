/* TreeitemMultipleSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 9, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation.select;

import java.util.Set;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * 
 * @author dennis
 * @author pao
 */
public class TreeitemMultipleSelectAgentBuilder extends AbstractMultipleSelectAgentBuilder {
	public MultipleSelectAgent getOperation(ComponentAgent target) {
			return new AbstractMultipleSelectAgentImpl(target) {
				@Override
				protected Tree getEventTarget() {
					return target.as(Treeitem.class).getTree();
				}

				@Override
				protected boolean isMultiple() {
					return getEventTarget().isMultiple();
				}

				@Override
				protected void collectSelectedItems(Set<String> selected) {
					for (Object item : getEventTarget().getSelectedItems())
						selected.add(((Treeitem) item).getUuid());
				}
			};
		}
		public Class<MultipleSelectAgent> getOperationClass() {
			return MultipleSelectAgent.class;
		}
	}