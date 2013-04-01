package de.benjaminborbe.authentication.api;

public class AuthenticationServiceException extends Exception {

	private static final long serialVersionUID = 660559509051108085L;

	public AuthenticationServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public AuthenticationServiceException(final String arg0) {
		super(arg0);
	}

	public AuthenticationServiceException(final Throwable arg0) {
		super(arg0);
	}

}
