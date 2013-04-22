/* AuEventAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 22, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent supports posting customized AU events.
 * @author pao
 * @since 1.1.0
 */
public interface AuEventAgent extends OperationAgent {

	void post(AuEvent... events);

}