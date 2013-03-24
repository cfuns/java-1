package de.benjaminborbe.dhl.util;

public class DhlStatusNotifierException extends Exception {

	private static final long serialVersionUID = 7330638735350675331L;

	public DhlStatusNotifierException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DhlStatusNotifierException(final String message) {
		super(message);
	}

	public DhlStatusNotifierException(final Throwable cause) {
		super(cause);
	}

}
