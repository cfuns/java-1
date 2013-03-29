package de.benjaminborbe.sample.api;

public class SampleServiceException extends Exception {

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
