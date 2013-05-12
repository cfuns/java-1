package de.benjaminborbe.selenium.api;

public class SeleniumServiceException extends Exception {

	private static final long serialVersionUID = -3041738690898323981L;

	public SeleniumServiceException(final Throwable cause) {
		super(cause);
	}

	public SeleniumServiceException(final String message) {
		super(message);
	}

	public SeleniumServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
