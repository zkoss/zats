/* MenuitemUploadAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Jun 25, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.UploadAgent;
import org.zkoss.zul.Menuitem;

/**
 * The upload agent builder of Menuitem.
 * @author pao
 */
public class MenuitemUploadAgentBuilder extends AbstractUploadAgentBuilder {

	public UploadAgent getOperation(ComponentAgent target) {
		return new MenuitemUploadAgentImpl(target);
	}

	class MenuitemUploadAgentImpl extends AbstractUploadAgentImpl {

		public MenuitemUploadAgentImpl(ComponentAgent target) {
			super(target);
		}

		@Override
		protected String getUploadFlag() {
			return target.as(Menuitem.class).getUpload();
		}
	}
}
