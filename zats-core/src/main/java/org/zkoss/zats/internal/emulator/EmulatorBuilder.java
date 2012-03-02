package org.zkoss.zats.internal.emulator;

import org.zkoss.zats.internal.emulator.impl.JettyEmulator;

/**
 * The builder for creating new emulator.
 * @author pao
 */
public class EmulatorBuilder
{
	private String contentPath;
	private String descriptor;

	/**
	 * Constructor.
	 * @param contentPath the web application root path. if null, throw IllegalArgumentException.
	 */
	public EmulatorBuilder(String contentPath)
	{
		if(contentPath == null)
			throw new IllegalArgumentException("contentPath can't be null.");
		this.contentPath = contentPath;
		this.descriptor = (contentPath + "/WEB-INF/web.xml").replaceAll("//+", "/");
	}

	/**
	 * specify the path of web.xml.
	 * default value is "./WEB-INF/web.xml".
	 * @param path specify path. If null, throw IllegalArgumentException.
	 * @return self reference.
	 */
	public EmulatorBuilder descriptor(String path)
	{
		if(path == null)
			throw new IllegalArgumentException("path can't be null");
		return this;
	}

	/**
	 * create new emulator using current configuration.
	 * @return a new emulator
	 */
	public Emulator create()
	{
		return new JettyEmulator(contentPath, descriptor);
	}
}
