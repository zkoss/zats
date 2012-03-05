package org.zkoss.zats.internal.emulator;

import java.io.File;
import java.net.URL;
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
	 * @param contentPath the web application root path.
	 */
	public EmulatorBuilder(String contentPath)
	{
		if(contentPath == null)
			throw new NullPointerException();
		this.contentPath = contentPath;
		this.descriptor = (contentPath + "/WEB-INF/web.xml").replaceAll("//+", "/");
	}

	/**
	 * Constructor.
	 * @param contentPath the web application root path.
	 */
	public EmulatorBuilder(File contentPath)
	{
		this(contentPath.getAbsolutePath());
	}

	/**
	 * specify the path of web.xml.
	 * default value is "./WEB-INF/web.xml".
	 * @param path specify path.
	 * @return self reference.
	 */
	public EmulatorBuilder descriptor(String path)
	{
		if(path == null)
			throw new NullPointerException();
		this.descriptor = path;
		return this;
	}

	/**
	 * specify the path of web.xml.
	 * default value is "./WEB-INF/web.xml".
	 * @param path URL of specify path.
	 * @return self reference.
	 */
	public EmulatorBuilder descriptor(URL path)
	{
		return descriptor(path.toString());
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
