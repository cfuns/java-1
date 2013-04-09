package de.benjaminborbe.storage.tools;

public class EntityIteratorException extends Exception {

	private static final long serialVersionUID = -1949267800168423780L;

	public EntityIteratorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EntityIteratorException(final String message) {
		super(message);
	}

	public EntityIteratorException(final Throwable cause) {
		super(cause);
	}

}
