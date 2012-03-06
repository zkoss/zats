package org.zkoss.zats.internal.emulator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zats.internal.emulator.impl.JettyEmulator;

/**
 * The builder for creating new emulator.
 * @author pao
 */
public class EmulatorBuilder
{
	private String contentRoot;
	private String descriptor;
	private List<String> resources;

	/**
	 * Constructor.
	 * @param contentRoot the web application root path.
	 */
	public EmulatorBuilder(String contentRoot)
	{
		if(contentRoot == null)
			throw new NullPointerException();
		this.contentRoot = contentRoot;
		this.descriptor = (contentRoot + "/WEB-INF/web.xml").replaceAll("//+", "/");
		this.resources = new ArrayList<String>();
		this.resources.add(contentRoot);
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
	 * add additional resource directory.
	 * @param resourceRoot directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addResource(String resourceRoot)
	{
		resources.add(resourceRoot);
		return this;
	}

	/**
	 * add additional resource directory.
	 * @param resourceRoot directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addResource(File resourceRoot)
	{
		return addResource(resourceRoot.getAbsolutePath());
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
		if(resources.size() <= 1)
			return new JettyEmulator(contentRoot, descriptor);
		else
			return new JettyEmulator(resources.toArray(new String[0]), descriptor);
	}
}
