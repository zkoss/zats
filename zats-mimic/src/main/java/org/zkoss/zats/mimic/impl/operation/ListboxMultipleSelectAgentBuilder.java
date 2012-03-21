/* ListboxMultipleSelectableBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashSet;
import java.util.Set;

import org.zkoss.zats.mimic.node.ComponentAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * 
 * @author dennis
 * 
 */
public class ListboxMultipleSelectAgentBuilder implements
		OperationAgentBuilder<MultipleSelectAgent> {
	public MultipleSelectAgent getOperation(final ComponentAgent target) {
		if (!target.is(Listbox.class)) {
			throw new RuntimeException("target cannot cast to listbox");
		}
		return new MultipleSelectAgent() {
			public MultipleSelectAgent select(int index) {
				Listbox listbox = target.as(Listbox.class);
				Set<Listitem> selitems = listbox.getSelectedItems();
				Set<String> sels = new HashSet<String>();
				for (Listitem item : selitems) {
					if (item.getIndex() == index) {
						return this;// skip
					}
					sels.add(item.getUuid());
				}
				String ref = listbox.getItemAtIndex(index).getUuid();
				sels.add(ref);
				AuUtility.postSelect(target, ref,
						sels.toArray(new String[sels.size()]));

				return this;
			}

			public MultipleSelectAgent deselect(int index) {
				Listbox listbox = target.as(Listbox.class);
				Set<Listitem> selitems = listbox.getSelectedItems();
				Set<String> sels = new HashSet<String>();
				boolean hit = false;
				for (Listitem item : selitems) {
					if (item.getIndex() == index) {
						hit = true;
						continue;
					}
					sels.add(item.getUuid());
				}
				if (!hit)
					return this;// skip
				String ref = listbox.getItemAtIndex(index).getUuid();
				AuUtility.postSelect(target, ref,
						sels.toArray(new String[sels.size()]));
				return this;
			}
		};
	}
}
