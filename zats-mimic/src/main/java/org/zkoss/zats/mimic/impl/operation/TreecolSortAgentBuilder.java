/* TreecolSortAgentBuilder.java

	Purpose:

	Description:

	History:
		2012/5/10 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.SortAgent;
import org.zkoss.zul.Treecol;

/**
 * Create SortAgent object for Treecol.
 * 
 * @author Hawk
 *
 */
public class TreecolSortAgentBuilder implements OperationAgentBuilder<ComponentAgent,SortAgent> {

	public SortAgent getOperation(ComponentAgent target) {
		return new SortAgentImpl(target);
	}

	public Class<SortAgent> getOperationClass() {
		return SortAgent.class;
	}

	private class SortAgentImpl extends SwitchedSortAgentImpl{

		public SortAgentImpl(ComponentAgent target) {
			super(target);
		}
		
		@Override
		public String getSortDirection() {
			return target.as(Treecol.class).getSortDirection();
		}

	}

}
