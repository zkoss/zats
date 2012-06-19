/* ButtonUploadAgent.java

	Purpose:
		
	Description:
		
	History:
		Jun 19, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.operation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.jetty.util.MultiPartOutputStream;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.operation.UploadAgent;

/**
 * 
 * @author pao
 * @since 1.1.0
 */
public class ButtonUploadAgent implements OperationAgentBuilder<ComponentAgent, UploadAgent> {

	public Class<UploadAgent> getOperationClass() {
		return UploadAgent.class;
	}

	public UploadAgent getOperation(ComponentAgent agent) {
		return null;
	}

	public static class UploadAgentImpl extends AgentDelegator<UploadAgent> implements UploadAgent {

		public UploadAgentImpl(UploadAgent target) {
			super(target);

		}

		public void upload(String fileName, InputStream content, String contentType) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				MultiPartOutputStream mpos = new MultiPartOutputStream(baos);
				System.out.println(mpos.getBoundary());
				mpos.startPart("text/plain");
				mpos.write("cool!\nThis is real multipart!".getBytes("UTF-8"));
				mpos.startPart("text/plain");
				mpos.write("Hello! World!\nHello! ZK!".getBytes("UTF-8"));
				mpos.close();
				System.out.println(new String(baos.toByteArray()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void upload(File file, String contentType) {
		}

		public void finish() {
		}

	}
}
