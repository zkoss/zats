/* AuEventAgent.java

	Purpose:
		
	Description:
		
	History:
		Apr 22, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

/**
 * The agent supports posting customized AU requests.
 * @author pao
 * @since 1.1.0
 */
public interface AuAgent extends OperationAgent {

	void post(AuData... auData);

}