/* MultipartUtility.java

	Purpose:

	Description:

	History:
		12:41 PM 2022/1/28, Created by jumperchen

Copyright (C) 2022 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import org.zkoss.zats.mimic.impl.operation.AbstractUploadAgentBuilder;

public class FormDataUtility {

	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;

	public FormDataUtility(HttpURLConnection httpConn, String charset) throws IOException {
		this.charset = charset;

		// creates a unique boundary based on time stamp
		boundary = "===" + System.currentTimeMillis() + "===";
		this.httpConn = httpConn;
		httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		outputStream = httpConn.getOutputStream();
	}

	private void write(String s) throws IOException {
		outputStream.write(s.getBytes(charset));
	}

	public void addFormField(String name, String value) throws IOException {
		write("--" + boundary + LINE_FEED);
		write("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
		write("Content-Type: text/plain; charset=" + charset + LINE_FEED);
		write(LINE_FEED);
		write(value + LINE_FEED);
		outputStream.flush();
	}

	public void addFilePart(String fieldName, AbstractUploadAgentBuilder.FileItem uploadFile) throws IOException {
		String fileName = uploadFile.getFileName();
		write("--" + boundary + LINE_FEED);
		write("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
		write("Content-Type: " + uploadFile.getContentType() + LINE_FEED);
		write("Content-Transfer-Encoding: binary" + LINE_FEED);
		write(LINE_FEED);
		outputStream.flush();

		InputStream inputStream = uploadFile.getInputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		write(LINE_FEED);
		outputStream.flush();
	}

	public void finish() throws IOException {
		write("--" + boundary + "--" + LINE_FEED);
		outputStream.flush();
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
