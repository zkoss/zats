package org.zkoss.zats.mimic.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZKExceptionHandler {
	private String zatsId;
	private List<Throwable> exceptions = new ArrayList<>();
	private static ConcurrentMap<String, ZKExceptionHandler> instances = new ConcurrentHashMap<>();

	public static ZKExceptionHandler getInstance(String zatsID) {
		Objects.requireNonNull(zatsID);
		return instances.computeIfAbsent(zatsID, key -> new ZKExceptionHandler(key));
	}

	public static void destroy(String zatsId) {
		ZKExceptionHandler zkExceptionHandler = instances.get(zatsId);
		if (zkExceptionHandler != null) {
			zkExceptionHandler.destroy();
		}
	}

	private ZKExceptionHandler(String zatsId) {
		this.zatsId = zatsId;
	}

	public void setExceptions(List l) {
		if (l == null || l.size() < 1)
			return;
		exceptions.addAll(l);
	}

	public List getExceptions() {
		return exceptions;
	}

	public void destroy() {
		if (exceptions != null && exceptions.size() > 0) {
			exceptions.clear();
		}
		instances.remove(zatsId);
	}
}
