/* ComponentAgentDelegator.java

	Purpose:
		
	Description:
		
	History:
		2012/3/21 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversation;

/**
 * Simply wrap the component agent for component operation
 * @author dennis
 * 
 */
public abstract class AgentDelegator implements Agent {
	protected ComponentAgent target;

	public AgentDelegator(ComponentAgent target) {
		this.target = target;
	}

	public Conversation getConversation() {
		return target.getConversation();
	}
	
	public Object getDelegatee(){
		return target.getDelegatee();
	}

}
