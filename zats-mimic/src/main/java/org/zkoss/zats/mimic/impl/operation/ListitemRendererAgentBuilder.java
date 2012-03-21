/* ListitemRendererAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/3/20 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.RendererAgent;
import org.zkoss.zul.Listitem;
/**
 * 
 * @author dennis
 *
 */
public class ListitemRendererAgentBuilder implements OperationAgentBuilder<RendererAgent> {
	public RendererAgent getOperation(final ComponentAgent target) {
		if(!target.is(Listitem.class)){
			throw new RuntimeException("target cannot transfer to listitem");
		}
		return new RendererAgentImpl(target);
	}
	
	class RendererAgentImpl extends AgentDelegator implements RendererAgent{
		public RendererAgentImpl(ComponentAgent delegatee) {
			super(delegatee);
		}

		public void render() {
			throw new RuntimeException("not implement yet");				
		};
	}
}
