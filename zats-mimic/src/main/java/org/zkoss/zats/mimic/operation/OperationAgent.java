/* OperationAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.operation;

import org.zkoss.zats.mimic.Agent;

/**
 * The base class of all operation agents that mimic user action. Testers use this class to mimic various user operations 
 * such as typing or clicking.
 * An operation mimics a client's action by sending AJAX Update to a testing application running in a emulator.
 * 
 * @author pao
 */
public interface OperationAgent extends Agent{

}
