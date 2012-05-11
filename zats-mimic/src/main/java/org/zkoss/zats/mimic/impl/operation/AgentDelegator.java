/* AgentDelegator.java

	Purpose:
		
	Description:
		
	History:
		2012/3/21 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.Client;

/**
 * Simply wrap the component agent for component operation
 * @author dennis
 * 
 */
public abstract class AgentDelegator<T extends Agent> implements Agent {
	protected T target;

	public AgentDelegator(T target) {
		this.target = target;
	}

	public Client getClient() {
		return target.getClient();
	}
	
	public Object getDelegatee(){
		return target.getDelegatee();
	}

}
