package de.benjaminborbe.api;

public class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = -5061582914947639839L;

	public NotImplementedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotImplementedException(final String message) {
		super(message);
	}

	public NotImplementedException(final Throwable cause) {
		super(cause);
	}

}
