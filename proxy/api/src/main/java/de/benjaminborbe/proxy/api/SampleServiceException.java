package de.benjaminborbe.proxy.api;

public class ProxyServiceException extends Exception {

	public ProxyServiceException(final Throwable cause) {
		super(cause);
	}

	public ProxyServiceException(final String message) {
		super(message);
	}

	public ProxyServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
