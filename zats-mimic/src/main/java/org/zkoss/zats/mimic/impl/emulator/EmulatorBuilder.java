/* EmulatorBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.emulator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.zkoss.zats.ZatsException;

/**
 * The builder for creating new emulator.
 * 
 * @author pao
 */
public class EmulatorBuilder {
	private static Logger logger = Logger.getLogger(EmulatorBuilder.class.getName());
	private String descriptor;//the path of web.xml
	private String contextPath = "/";
	
	private Resource webWebInf;//the first resource that contains WEB-INF
	private List<Resource> contentRoots;//the paths of content root

	public EmulatorBuilder() {
		this.contentRoots = new ArrayList<Resource>();
	}

	public EmulatorBuilder setWebInf(String webInfPathOrUrl) {
		webWebInf = new WebWebInfResource(toResource(webInfPathOrUrl));
		return this;
	}
	
	public EmulatorBuilder setWebInf(URL webInf) {
		webWebInf = new WebWebInfResource(toResource(webInf));
		return this;
	}

	/**
	 * add additional resource root directory.
	 * 
	 * @param contentRootPathOrUrl directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(String contentRootPathOrUrl) {
		contentRoots.add(toResource(contentRootPathOrUrl));
		return this;
	}
	/**
	 * add additional resource root directory.
	 * 
	 * @param contentRoot directory url.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(URL contentRoot) {
		contentRoots.add(toResource(contentRoot));
		return this;
	}
	
	private Resource toResource(String pathOrUrl){
		try {
			return ResourceFactory.root().newResource(pathOrUrl);
		} catch (Exception x) {
			throw new EmulatorException(x.getMessage(),x);
		}
	}
	
	private Resource toResource(URL url){
		try {
			return ResourceFactory.root().newResource(url.toURI());
		} catch (Exception x) {
			throw new EmulatorException(x.getMessage(),x);
		}
	}

	/**
	 * specify the path of web.xml. default value is "./WEB-INF/web.xml".
	 * 
	 * @param descriptor path of web.xml
	 * @return self reference.
	 */
	public EmulatorBuilder setDescriptor(String descriptor) {
		//if a descriptor is null, it use the file in content root
		this.descriptor = descriptor;
		return this;
	}
	
	
	public EmulatorBuilder setContextPath(String contextPath) {
		if(contextPath==null)
			throw new IllegalArgumentException("null context path");
		this.contextPath = contextPath;
		return this;
	}

	/**
	 * create new emulator using current configuration.
	 * 
	 * @return a new emulator
	 */
	public Emulator create() {
		if(webWebInf==null && contentRoots.size()==0)
			throw new ZatsException("no content root found");
		List<Resource> lr;
		
		if(webWebInf!=null){//insert the webWebInfo
			lr = new ArrayList<Resource>(contentRoots);
			lr.add(0, webWebInf);
		}else{
			lr = contentRoots;
		}
		
		return new JettyEmulator(lr.toArray(new Resource[lr.size()]),
					descriptor,contextPath);
	}
	
	static class WebWebInfResource extends Resource {

		Resource webInf;
		
		public WebWebInfResource(Resource webInf){
			this.webInf = webInf;
		}
		
		@Override
		public Resource resolve(String path) {
			if (path == null)
				return null;
			String p = URIUtil.canonicalPath(path);
			p = p.startsWith("/") ? p.substring(1) : p;

			if (p.startsWith("WEB-INF/")) {
				p = p.substring("WEB-INF/".length());
				return webInf.resolve(p);
			}
			if (p.equals("WEB-INF")) {
				return webInf;
			}

			return toNonExist(path);//the bad resource
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public boolean isReadable() {
			return true;
		}

		@Override
		public boolean isDirectory() {
			return true;
		}

		@Override
		public Instant lastModified() {
			return Instant.EPOCH;
		}

		@Override
		public long length() {
			return -1;
		}

		@Override
		public URI getURI() {
			try {
				URI wuri = webInf.getURI();
				if(wuri==null) return null;
				String wp = wuri.toASCIIString();
				//the parent url
				if(wp.endsWith("/")){
					wp = wp.substring(0,wp.length()-1);
				}
				wp = wp.substring(0,wp.lastIndexOf('/')+1);
				return new URI(wp);
			} catch (URISyntaxException e) {
				logger.warning(e.getMessage());
			}
			return null;
		}

		@Override
		public String getName() {
			return getFileName();
		}

		@Override
		public String getFileName() {
			URI uri = getURI();
			if (uri != null) {
				String path = uri.getPath();
				if (path != null) {
					if (path.endsWith("/")) {
						path = path.substring(0, path.length() - 1);
					}
					int last = path.lastIndexOf('/');
					if (last >= 0) {
						return path.substring(last + 1);
					}
					return path;
				}
			}
			return "Unknown";
		}

		@Override
		public InputStream newInputStream() throws IOException {
			throw new IOException("cannot open input stream in virtual folder");
		}

		@Override
		public ReadableByteChannel newReadableByteChannel() throws IOException {
			return null;
		}

		@Override
		public List<Resource> list() {
			return Collections.singletonList(webInf);
		}

		@Override
		public Path getPath() {
			return null;
		}

		Resource toNonExist(String path){
			String tmpDir = System.getProperty("java.io.tmpdir", ".");
			try {
				return ResourceFactory.root().newResource(new File(tmpDir,"zats/non_exist/"+path).toPath());
			} catch (Exception x) {
				logger.warning(x.getMessage());
				throw new EmulatorException(x.getMessage(),x);
			}
		}
		
		public String toString(){
			URI uri = getURI();
			return uri==null?super.toString():uri.toASCIIString();
		}
		
	}
	
}
