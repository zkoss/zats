package org.zkoss.zats.mimic.internal.emulator;

public class EmulatorException extends RuntimeException
{
	private static final long serialVersionUID = 2958409858718318588L;

	public EmulatorException(String message)
	{
		super(message);
	}

	public EmulatorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
