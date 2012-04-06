/* ListboxSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.impl.operation.GenericSelectAgentBuilder.SingleSelectAgentImpl;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 *  The builder for selection agent of listbox.
 * @author pao
 */
public class ListboxSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {

	public SelectAgent getOperation(final ComponentAgent target) {
		return new SelectAgentImpl(target);
	}

	class SelectAgentImpl extends SingleSelectAgentImpl {
		public SelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		public void select() {
			if (!target.is(Listitem.class))
				throw new RuntimeException(target + " can't select");

			// check if parent component is at multiple selection mode
			Listbox listbox = target.as(Listitem.class).getListbox();
			if (listbox.isMultiple()) {
				Set<String> selected = getSelectedItems(listbox);
				if (!selected.contains(target.getUuid())) {
					selected.add(target.getUuid());
					post(listbox, selected);
				} else
					return; // skip, target was already selected.
			} else
				super.select();
		}

		@Override
		public void deselect() {
			if (!target.is(Listitem.class))
				throw new RuntimeException(target + " can't select");

			// check if parent component is at multiple selection mode
			Listbox listbox = target.as(Listitem.class).getListbox();
			if (listbox.isMultiple()) {

				Set<String> selected = getSelectedItems(listbox);
				if (selected.contains(target.getUuid())) {
					selected.remove(target.getUuid());
					post(listbox, selected);
				} else
					return; // skip, target wasn't selected.
			} else
				super.deselect();
		}

		private void post(Listbox listbox, Set<String> selected) {
			String desktopId = target.getDesktop().getId();
			Event event = new SelectEvent(Events.ON_SELECT, listbox, selected, target.getComponent());
			Map<String, Object> data = EventDataManager.build(event);
			((ConversationCtrl) target.getConversation()).postUpdate(desktopId, listbox.getUuid(), event.getName(), data);
		}

		@SuppressWarnings("unchecked")
		private Set<String> getSelectedItems(Listbox listbox) {
			Set<String> selected = new HashSet<String>();
			Set<Listitem> items = listbox.getSelectedItems();
			for (Listitem item : items)
				selected.add(item.getUuid());
			return selected;
		}
	}
}
