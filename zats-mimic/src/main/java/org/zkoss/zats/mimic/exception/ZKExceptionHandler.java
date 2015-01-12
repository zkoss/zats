package org.zkoss.zats.mimic.exception;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ZKExceptionHandler {
	private static ZKExceptionHandler currentHandler;
	private static CopyOnWriteArrayList<Throwable> exceptions;
	
	public static ZKExceptionHandler getInstance() {
		if (currentHandler == null) {
			synchronized(ZKExceptionHandler.class) {
				if (currentHandler == null) {
					currentHandler = new ZKExceptionHandler();
					exceptions = new CopyOnWriteArrayList<Throwable>();
				}
			}
		}
		return currentHandler;
	}
	
	private ZKExceptionHandler () {}
	
	public void setExceptions(List l) {
		if (l == null || l.size() < 1)
			return;
		exceptions.addAll(l);
	}
	
	public List getExceptions() {
		return exceptions;
	}
	
	public void destroy() {
		if (exceptions.size() > 0) {
			exceptions.clear();
		}
	}

}
