/* ListboxMultipleSelectableBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.MultipleSelectable;
import org.zkoss.zats.mimic.operation.Selectable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
/**
 * 
 * @author dennis
 *
 */
public class ListboxMultipleSelectableBuilder implements OperationBuilder<MultipleSelectable> {
	public MultipleSelectable getOperation(final ComponentNode target) {
		if(!target.isCastable(Listbox.class)){
			throw new RuntimeException("target cannot cast to listbox");
		}
		return new MultipleSelectable() {
			public MultipleSelectable select(int index) {
				Listbox listbox = target.cast(Listbox.class);
				Set<Listitem> selitems = listbox.getSelectedItems();
				Set<String> sels = new HashSet<String>();
				for(Listitem item:selitems){
					if(item.getIndex()==index){
						return this;//skip
					}
					sels.add(item.getUuid());
				}
				String ref = listbox.getItemAtIndex(index).getUuid();
				sels.add(ref);
				OperationUtil.doSelect(target, ref,sels.toArray(new String[sels.size()]));
				
				
				return this;
			}

			public MultipleSelectable deselect(int index) {
				Listbox listbox = target.cast(Listbox.class);
				Set<Listitem> selitems = listbox.getSelectedItems();
				Set<String> sels = new HashSet<String>();
				boolean hit = false;
				for(Listitem item:selitems){
					if(item.getIndex()==index){
						hit = true;
						continue;
					}
					sels.add(item.getUuid());
				}
				if(!hit) return this;//skip
				String ref = listbox.getItemAtIndex(index).getUuid();
				OperationUtil.doSelect(target, ref,sels.toArray(new String[sels.size()]));
				return this;
			}
		};
	}
}
