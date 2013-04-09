package de.benjaminborbe.websearch.api;

public class WebsearchServiceException extends Exception {

	private static final long serialVersionUID = 7438928590162792446L;

	public WebsearchServiceException(final String message) {
		super(message);
	}

	public WebsearchServiceException(final Throwable cause) {
		super(cause);
	}

	public WebsearchServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
