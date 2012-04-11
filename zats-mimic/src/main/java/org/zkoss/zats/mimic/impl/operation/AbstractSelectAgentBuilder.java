/* GenericSelectAgentBuilder.java

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
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Treeitem;

/**
 *  The builder for selection agent.
 * @author pao
 */
public class AbstractSelectAgentBuilder {

	static abstract class SingleSelectAgentImpl extends AgentDelegator implements SelectAgent {
		public SingleSelectAgentImpl(ComponentAgent target) {
			super(target);
		}

		abstract protected Component getTargetForPosting();

		protected void postprocess(Map<String, Object> data) {
			// override by child class
		}

		public void select() {
			Set<String> items = new HashSet<String>();
			items.add(target.getUuid());

			Component ancestry = getTargetForPosting();
			Event event = new SelectEvent(Events.ON_SELECT, ancestry, items, target.getComponent());
			Map<String, Object> data = EventDataManager.build(event);
			postprocess(data);
			((ClientCtrl) target.getClient()).postUpdate(target.getDesktop().getId(), ancestry.getUuid(),
					event.getName(), data);
		}

		public void deselect() {
			throw new RuntimeException(target + " can't deselect");
		}
	}

	static class ComboitemSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {
		public SelectAgent getOperation(ComponentAgent target) {
			return new SingleSelectAgentImpl(target) {
				@Override
				protected Component getTargetForPosting() {
					return target.as(Comboitem.class).getParent(); // combobox
				}
			};
		}
	}

	static class TabSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {
		public SelectAgent getOperation(ComponentAgent target) {
			return new SingleSelectAgentImpl(target) {
				@Override
				protected Component getTargetForPosting() {
					return target.getComponent();
				}
			};
		}
	}

	static class LisitemSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {
		public SelectAgent getOperation(ComponentAgent target) {
			return new SingleSelectAgentImpl(target) {
				@Override
				protected Component getTargetForPosting() {
					return target.as(Listitem.class).getListbox();
				}

				@Override
				protected void postprocess(Map<String, Object> data) {
					data.put("clearFirst", target.as(Listitem.class).getListbox().isMultiple());
				}
			};
		}
	}

	static class TreeSelectAgentBuilder implements OperationAgentBuilder<SelectAgent> {
		public SelectAgent getOperation(ComponentAgent target) {
			return new SingleSelectAgentImpl(target) {
				@Override
				protected Component getTargetForPosting() {
					return target.as(Treeitem.class).getTree();
				}

				@Override
				protected void postprocess(Map<String, Object> data) {
					data.put("clearFirst", target.as(Treeitem.class).getTree().isMultiple());
				}
			};
		}
	}
}
