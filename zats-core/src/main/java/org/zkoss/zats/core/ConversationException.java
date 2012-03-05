package org.zkoss.zats.core;

public class ConversationException extends RuntimeException
{
	private static final long serialVersionUID = -307047315005722126L;

	public ConversationException(String message)
	{
		super(message);
	}

	public ConversationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
