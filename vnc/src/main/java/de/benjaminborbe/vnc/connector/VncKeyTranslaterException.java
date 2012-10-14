package de.benjaminborbe.vnc.connector;

public class VncKeyTranslaterException extends Exception {

	private static final long serialVersionUID = -8850956807235507096L;

	public VncKeyTranslaterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public VncKeyTranslaterException(final String message) {
		super(message);
	}

	public VncKeyTranslaterException(final Throwable cause) {
		super(cause);
	}

}
