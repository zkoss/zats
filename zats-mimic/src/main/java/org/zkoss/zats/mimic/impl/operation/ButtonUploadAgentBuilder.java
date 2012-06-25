/* ButtonUploadAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Jun 25, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zul.Button;

/**
 * The upload agent builder of button components.
 * @author pao
 */
public class ButtonUploadAgentBuilder extends AbstractUploadAgentBuilder {

	public UploadAgent getOperation(ComponentAgent target) {
		return new ButtonUploadAgentImpl(target);
	}

	class ButtonUploadAgentImpl extends AbstractUploadAgentImpl {

		public ButtonUploadAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected String getUploadFlag() {
			return target.as(Button.class).getUpload();
		}
	}
}
