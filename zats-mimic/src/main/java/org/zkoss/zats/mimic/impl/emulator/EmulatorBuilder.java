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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
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
		webWebInf = new WebWebInfResource(toRecource(webInfPathOrUrl));
		return this;
	}
	
	public EmulatorBuilder setWebInf(URL webInf) {
		webWebInf = new WebWebInfResource(toRecource(webInf));
		return this;
	}

	/**
	 * add additional resource root directory.
	 * 
	 * @param contentRootPathOrUrl directory path.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(String contentRootPathOrUrl) {
		contentRoots.add(toRecource(contentRootPathOrUrl));
		return this;
	}
	/**
	 * add additional resource root directory.
	 * 
	 * @param contentRoot directory url.
	 * @return self reference.
	 */
	public EmulatorBuilder addContentRoot(URL contentRoot) {
		contentRoots.add(toRecource(contentRoot));
		return this;
	}
	
	private Resource toRecource(String pathOrUrl){
		try {
			return Resource.newResource(pathOrUrl);
		} catch (Exception x) {
			throw new EmulatorException(x.getMessage(),x);
		}
	}
	
	private Resource toRecource(URL url){
		try {
			return Resource.newResource(url);
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
	
	static class WebWebInfResource extends Resource{

		Resource webInf;
		
		public WebWebInfResource(Resource webInf){
			this.webInf = webInf;
		}
		
		@Override
		public boolean isContainedIn(Resource r) throws MalformedURLException {
			URL wurl = webInf.getURI().toURL();
			URL rurl = r.getURI().toURL();
			if(wurl==null||rurl==null) return false;
			String wp = wurl.toExternalForm();
			String rp = rurl.toExternalForm();
			return rp.startsWith(wp);
		}

		@Override
		public void close() {
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public boolean isDirectory() {
			return true;
		}

		@Override
		public long lastModified() {
			return -1;
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
				String wp = wuri.toURL().toExternalForm();
				//the parent url
				if(wp.endsWith("/")){
					wp = wp.substring(0,wp.length()-1);
				}
				wp = wp.substring(0,wp.lastIndexOf('/')+1);
				return new URI(wp);
			} catch (MalformedURLException | URISyntaxException e) {
				logger.warning(e.getMessage());
			}
			return null;
		}

		@Override
		public File getFile() throws IOException {
			URL url = getURI().toURL();
			File f = url==null?null:new File(url.getFile());
			return f;
		}

		@Override
		public String getName() {
			try {
				File f = getFile();
				if(f!=null) return f.getName();
			} catch (IOException e) {
				logger.warning(e.getMessage());
			}
			return "Unknow";
		}

		@Override
		public InputStream getInputStream() throws IOException {
			throw new IOException("cannot open input stream in virtual folder");
		}

		@Override
		public ReadableByteChannel getReadableByteChannel() throws IOException {
			return null;
		}

		@Override
		public boolean delete() throws SecurityException {
			return false;
		}

		@Override
		public boolean renameTo(Resource dest) throws SecurityException {
			return false;
		}

		@Override
		public String[] list() {
			return new String[]{"WEB-INF"};
		}

		@Override
		public Resource addPath(String path) throws IOException, MalformedURLException {
			if (path==null)
	            return null;
	        String p = URIUtil.canonicalPath(path);
	        p = p.startsWith("/")?p.substring(1):p;
			
			if(p.startsWith("WEB-INF/")){
				p = p.substring("WEB-INF".length());
				return webInf.addPath(p);
			}
			
			return toNonExist(path);//the bad resource
		}
		
		
		
		Resource toNonExist(String path){
			String tmpDir = System.getProperty("java.io.tmpdir", ".");
			try {
				return new PathResource(new File(tmpDir,"zats/non_exist/"+path).toURL());
			} catch (Exception x) {
				logger.warning(x.getMessage());
				throw new EmulatorException(x.getMessage(),x);
			}
		}
		
		public String toString(){
			URL url = null;
			try {
				url = getURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return url==null?super.toString():url.toExternalForm();
		}
		
	}
	
}
