package de.benjaminborbe.authorization.api;

public class AuthorizationServiceException extends Exception {

	private static final long serialVersionUID = -2913415428966436277L;

	public AuthorizationServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public AuthorizationServiceException(final String arg0) {
		super(arg0);
	}

	public AuthorizationServiceException(final Throwable arg0) {
		super(arg0);
	}

}
