/* WindowSizeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/9 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.SizeAgent;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * @author dennis
 *
 */
public class PanelSizeAgentBuilder extends AbstractSizeAgentBuilder{

	public SizeAgent getOperation(ComponentAgent agent) {
		return new SizeAgentImpl(agent);
	}

	static class SizeAgentImpl extends AbstractSizeAgentImpl{

		public SizeAgentImpl(ComponentAgent target) {
			super(target);
		}
		
		private Panel getComponent(){
			return target.as(Panel.class);
		}

		@Override
		protected boolean isMaximizable() {
			return getComponent().isMaximizable();
		}

		@Override
		protected boolean isMinimizable() {
			return getComponent().isMinimizable();
		}

		@Override
		protected boolean isSizable() {
			return getComponent().isSizable();
		}

		@Override
		protected int getMinwidth() {
			return getComponent().getMinwidth();
		}

		@Override
		protected int getMinheight() {
			return getComponent().getMinheight();
		}
		
	}
}
