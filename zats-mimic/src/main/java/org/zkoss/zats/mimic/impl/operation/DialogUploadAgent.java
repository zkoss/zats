/* DialogUploadAgent.java

	Purpose:
		
	Description:
		
	History:
		Jun 19, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.UploadAgent;

/**
 * 
 * @author pao
 * @since 1.1.0
 */
public class DialogUploadAgent implements OperationAgentBuilder<DesktopAgent, UploadAgent> {

	public Class<UploadAgent> getOperationClass() {
		return UploadAgent.class;
	}

	public UploadAgent getOperation(DesktopAgent agent) {
		return null;
	}
}
