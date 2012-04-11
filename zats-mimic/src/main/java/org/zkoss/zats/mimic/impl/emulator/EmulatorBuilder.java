/* EmulatorBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.emulator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The builder for creating new emulator.
 * 
 * @author pao
 */
public class EmulatorBuilder {
//	private String contentRoot;
	private String descriptor;
	private List<String> contentRoots;

	/**
	 * Constructor.
	 * 
	 * @param contentFolder
	 *            the web application root path.
	 */
	public EmulatorBuilder(String contentFolder) {
		if (contentFolder == null)
			throw new NullPointerException();
//		this.contentRoot = contentFolder;
		this.descriptor = (contentFolder + "/WEB-INF/web.xml").replaceAll("//+",
				"/");
		this.contentRoots = new ArrayList<String>();
		this.contentRoots.add(contentFolder);
	}

	/**
	 * Constructor.
	 * 
	 * @param contentFolder
	 *            the web application root path.
	 */
	public EmulatorBuilder(File contentFolder) {
		this(contentFolder.getAbsolutePath());
	}

	/**
	 * add additional resource root directory.
	 * 
	 * @param resourceRoot
	 *            directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(String contentRoot) {
		contentRoots.add(contentRoot);
		return this;
	}

	/**
	 * add additional resource root directory.
	 * 
	 * @param resourceRoot
	 *            directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(File resourceRoot) {
		return addContentRoot(resourceRoot.getAbsolutePath());
	}

	/**
	 * specify the path of web.xml. default value is "./WEB-INF/web.xml".
	 * 
	 * @param path
	 *            specify path.
	 * @return self reference.
	 */
	public EmulatorBuilder descriptor(String path) {
		if (path == null)
			throw new NullPointerException();
		this.descriptor = path;
		return this;
	}

	/**
	 * specify the path of web.xml. default value is "./WEB-INF/web.xml".
	 * 
	 * @param path
	 *            URL of specify path.
	 * @return self reference.
	 */
	public EmulatorBuilder descriptor(URL path) {
		return descriptor(path.toString());
	}

	/**
	 * create new emulator using current configuration.
	 * 
	 * @return a new emulator
	 */
	public Emulator create() {
		return new JettyEmulator(contentRoots.toArray(new String[contentRoots.size()]),
					descriptor,"/");
	}
}
