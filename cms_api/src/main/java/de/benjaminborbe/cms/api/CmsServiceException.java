package de.benjaminborbe.cms.api;

public class CmsServiceException extends Exception {

	private static final long serialVersionUID = -5241207548976332271L;

	public CmsServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CmsServiceException(final String message) {
		super(message);
	}

	public CmsServiceException(final Throwable cause) {
		super(cause);
	}

}
