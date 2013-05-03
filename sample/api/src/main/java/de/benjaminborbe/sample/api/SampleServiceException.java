package de.benjaminborbe.sample.api;

public class SampleServiceException extends Exception {

	private static final long serialVersionUID = -3041738690898323981L;

	public SampleServiceException(final Throwable cause) {
		super(cause);
	}

	public SampleServiceException(final String message) {
		super(message);
	}

	public SampleServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
