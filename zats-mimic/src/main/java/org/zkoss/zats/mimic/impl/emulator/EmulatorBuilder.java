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

import org.zkoss.zats.ZatsException;

/**
 * The builder for creating new emulator.
 * 
 * @author pao
 */
public class EmulatorBuilder {
	private String descriptor;//the path of web.xml
	private String contextPath = "/";
	private List<String> contentRoots;//the paths of content root

	/**
	 * Constructor.
	 * 
	 * @param contentFolder
	 *            the web application root path.
	 */
	public EmulatorBuilder() {
		this.contentRoots = new ArrayList<String>();
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
	 * specify the path of web.xml. default value is "./WEB-INF/web.xml".
	 * 
	 * @param path
	 *            specify path.
	 * @return self reference.
	 */
	public EmulatorBuilder setDescriptor(String descriptor) {
		//if a descriptor is null, it use the file in content root
		this.descriptor = descriptor;
		return this;
	}
	
	
	public EmulatorBuilder setContextPath(String contextPath) {
		if(contextPath==null)
			throw new ZatsException("unll context path");
		this.contextPath = contextPath;
		return this;
	}

	/**
	 * create new emulator using current configuration.
	 * 
	 * @return a new emulator
	 */
	public Emulator create() {
//		if(descriptor==null)
//			throw new ZatsException("web.xml url not found");
		if(contentRoots.size()==0)
			throw new ZatsException("not content root found");
		
		return new JettyEmulator(contentRoots.toArray(new String[contentRoots.size()]),
					descriptor,contextPath);
	}
}
