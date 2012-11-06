package de.benjaminborbe.api;

public class IdentifierBuilderException extends Exception {

	private static final long serialVersionUID = 7695284741372655823L;

	public IdentifierBuilderException(final String message) {
		super(message);
	}

	public IdentifierBuilderException(final Throwable cause) {
		super(cause);
	}

	public IdentifierBuilderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
