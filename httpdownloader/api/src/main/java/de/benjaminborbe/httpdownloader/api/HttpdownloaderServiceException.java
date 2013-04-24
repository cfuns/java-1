package de.benjaminborbe.httpdownloader.api;

public class HttpdownloaderServiceException extends Exception {

	public HttpdownloaderServiceException(final Throwable cause) {
		super(cause);
	}

	public HttpdownloaderServiceException(final String message) {
		super(message);
	}

	public HttpdownloaderServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
