package org.zkoss.zats.mimic.exception;

import java.util.ArrayList;
import java.util.List;

public class ZKExceptionHandler {
	private static final ThreadLocal<ZKExceptionHandler> _handler = new ThreadLocal<>();
	private final List<Throwable> _exceptions = new ArrayList<>();

	public static ZKExceptionHandler getInstance() {
		ZKExceptionHandler handler = _handler.get();
		if (handler == null) {
			handler = new ZKExceptionHandler();
			_handler.set(handler);
		}
		return handler;
	}

	private ZKExceptionHandler() {
	}

	public void setExceptions(List l) {
		if (l == null || l.size() < 1)
			return;
		_exceptions.addAll(l);
	}

	public List getExceptions() {
		return _exceptions;
	}

	public void destroy() {
		if (_exceptions != null && _exceptions.size() > 0) {
			_exceptions.clear();
		}
	}
}
