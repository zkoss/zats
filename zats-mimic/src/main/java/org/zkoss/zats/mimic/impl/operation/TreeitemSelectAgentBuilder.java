/* TreeitemSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 6, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ConversationCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 *  The builder for selection agent of Treeitem.
 * @author pao
 */
public class TreeitemSelectAgentBuilder implements OperationAgentBuilder<MultipleSelectAgent> {

	public MultipleSelectAgent getOperation(final ComponentAgent target) {
		return new SelectAgentImpl(target);
	}

	class SelectAgentImpl extends AgentDelegator implements MultipleSelectAgent {
		public SelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void select() {
			if (!target.is(Treeitem.class))
				throw new RuntimeException(target + " can't select");

			// check if parent component is at multiple selection mode
			Tree tree = target.as(Treeitem.class).getTree();
			if (tree.isMultiple()) {
				Set<String> selected = getSelectedItems(tree);
				if (!selected.contains(target.getUuid())) {
					selected.add(target.getUuid());
					post(tree, selected);
				} else
					return; // skip, target was already selected.
			} else {
				throw new RuntimeException(target + " isn't multiple selection mode");
			}
		}

		public void deselect() {
			if (!target.is(Treeitem.class))
				throw new RuntimeException(target + " can't select");

			// check if parent component is at multiple selection mode
			Tree tree = target.as(Treeitem.class).getTree();
			if (tree.isMultiple()) {

				Set<String> selected = getSelectedItems(tree);
				if (selected.contains(target.getUuid())) {
					selected.remove(target.getUuid());
					post(tree, selected);
				} else
					return; // skip, target wasn't selected.
			} else {
				throw new RuntimeException(target + " isn't multiple selection mode");
			}
		}

		private void post(Tree tree, Set<String> selected) {
			String desktopId = target.getDesktop().getId();
			Event event = new SelectEvent(Events.ON_SELECT, tree, selected, target.getComponent());
			Map<String, Object> data = EventDataManager.build(event);
			((ConversationCtrl) target.getConversation()).postUpdate(desktopId, tree.getUuid(), event.getName(), data);
		}

		@SuppressWarnings("unchecked")
		private Set<String> getSelectedItems(Tree tree) {
			Set<String> selected = new HashSet<String>();
			Set<Treeitem> items = tree.getSelectedItems();
			for (Treeitem item : items)
				selected.add(item.getUuid());
			return selected;
		}
	}
}
