package org.zkoss.zats.mimic.exception;

import org.zkoss.lang.Strings;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZKExceptionHandler {
	private static final ConcurrentMap<String, List<Throwable>> _exceptions = new ConcurrentHashMap<>();

	public static void putExceptions(String url, List l) {
		if (Strings.isEmpty(url) || l == null || l.size() < 1)
			return;
		_exceptions.put(url, l);
	}

	public static List getExceptions(String url) {
		return _exceptions.get(url);
	}

	public static void destroy(String url) {
		_exceptions.remove(url);
	}
}
