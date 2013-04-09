package de.benjaminborbe.vnc.api;

public class VncServiceException extends Exception {

	private static final long serialVersionUID = 3404836944446141997L;

	public VncServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public VncServiceException(final String message) {
		super(message);
	}

	public VncServiceException(final Throwable cause) {
		super(cause);
	}

}
