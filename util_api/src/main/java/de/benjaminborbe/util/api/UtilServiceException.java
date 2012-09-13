package de.benjaminborbe.util.api;

public class UtilServiceException extends Exception {

	private static final long serialVersionUID = -5776747320086374131L;

	public UtilServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UtilServiceException(final String message) {
		super(message);
	}

	public UtilServiceException(final Throwable cause) {
		super(cause);
	}

}
