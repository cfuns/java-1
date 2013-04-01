package de.benjaminborbe.authentication.core.util;

public class AuthenticationGeneratePasswordFailedException extends Exception {

	private static final long serialVersionUID = -7400489725166167857L;

	public AuthenticationGeneratePasswordFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AuthenticationGeneratePasswordFailedException(final String message) {
		super(message);
	}

	public AuthenticationGeneratePasswordFailedException(final Throwable cause) {
		super(cause);
	}

}
