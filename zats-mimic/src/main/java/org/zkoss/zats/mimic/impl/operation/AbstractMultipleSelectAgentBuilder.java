/* AbstractMultipleSelectAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Apr 9, 2012 Created by pao

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * @author pao
 *
 */
public class AbstractMultipleSelectAgentBuilder {
	static abstract class MultipleSelectAgentImpl extends AgentDelegator implements MultipleSelectAgent {
		public MultipleSelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void select() {
			// check if parent component is at multiple selection mode
			if (isMultiple()) {
				Set<String> selected = new HashSet<String>();
				putSelectedItems(selected);
				if (!selected.contains(target.getUuid())) {
					selected.add(target.getUuid());
					post(getTargetForPosting(), selected);
				} else
					return; // skip, target was already selected.
			} else {
				throw new RuntimeException(target + " isn't multiple selection mode");
			}
		}

		public void deselect() {
			// check if parent component is at multiple selection mode
			if (isMultiple()) {
				Set<String> selected = new HashSet<String>();
				putSelectedItems(selected);
				if (selected.contains(target.getUuid())) {
					selected.remove(target.getUuid());
					post(getTargetForPosting(), selected);
				} else
					return; // skip, target wasn't selected.
			} else {
				throw new RuntimeException(target + " isn't multiple selection mode");
			}
		}

		private void post(Component component, Set<String> selected) {
			String desktopId = target.getDesktop().getId();
			Event event = new SelectEvent(Events.ON_SELECT, component, selected, target.getComponent());
			Map<String, Object> data = EventDataManager.build(event);
			ConversationCtrl ctrl = (ConversationCtrl) target.getConversation();
			ctrl.postUpdate(desktopId, component.getUuid(), event.getName(), data);
		}

		abstract protected Component getTargetForPosting();

		abstract protected boolean isMultiple();

		abstract protected void putSelectedItems(Set<String> selected);
	}

	static class ListitemMultipleSelectAgentBuilder implements OperationAgentBuilder<MultipleSelectAgent> {
		public MultipleSelectAgent getOperation(ComponentAgent target) {
			return new MultipleSelectAgentImpl(target) {
				@Override
				protected Listbox getTargetForPosting() {
					if (!target.is(Listitem.class))
						throw new RuntimeException(target + " can't select");
					return target.as(Listitem.class).getListbox();
				}

				@Override
				protected boolean isMultiple() {
					return getTargetForPosting().isMultiple();
				}

				@Override
				protected void putSelectedItems(Set<String> selected) {
					for (Object item : getTargetForPosting().getSelectedItems())
						selected.add(((Listitem) item).getUuid());
				}
			};
		}
	}

	static class TreeitemMultipleSelectAgentBuilder implements OperationAgentBuilder<MultipleSelectAgent> {
		public MultipleSelectAgent getOperation(ComponentAgent target) {
			return new MultipleSelectAgentImpl(target) {
				@Override
				protected Tree getTargetForPosting() {
					if (!target.is(Treeitem.class))
						throw new RuntimeException(target + " can't select");
					return target.as(Treeitem.class).getTree();
				}

				@Override
				protected boolean isMultiple() {
					return getTargetForPosting().isMultiple();
				}

				@Override
				protected void putSelectedItems(Set<String> selected) {
					for (Object item : getTargetForPosting().getSelectedItems())
						selected.add(((Treeitem) item).getUuid());
				}
			};
		}
	}
}
