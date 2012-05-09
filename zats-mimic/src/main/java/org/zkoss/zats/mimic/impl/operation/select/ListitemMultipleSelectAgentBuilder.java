/* ListitemMultipleSelectAgentBuilder.java

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * 
 * @author dennis
 * @author pao
 */
public class ListitemMultipleSelectAgentBuilder extends AbstractMultipleSelectAgentBuilder {
	public MultipleSelectAgent getOperation(ComponentAgent target) {
		return new AbstractMultipleSelectAgentImpl(target) {
			@Override
			protected Listbox getEventTarget() {
				return target.as(Listitem.class).getListbox();
			}

			@Override
			protected boolean isMultiple() {
				return getEventTarget().isMultiple();
			}

			@Override
			protected void collectSelectedItems(Set<String> selected) {
				for (Object item : getEventTarget().getSelectedItems())
					selected.add(((Listitem) item).getUuid());
			}
		};
	}

}