package de.benjaminborbe.lunch.util;

public class LunchUserNotifierException extends Exception {

	private static final long serialVersionUID = -6481763479606539227L;

	public LunchUserNotifierException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public LunchUserNotifierException(final String message) {
		super(message);
	}

	public LunchUserNotifierException(final Throwable cause) {
		super(cause);
	}

}
