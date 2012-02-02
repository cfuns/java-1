package de.benjaminborbe.tools.dao;

public class PrimaryKeyMissingException extends RuntimeException {

	private static final long serialVersionUID = 6081661259780408985L;

	public PrimaryKeyMissingException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public PrimaryKeyMissingException(final String arg0) {
		super(arg0);
	}

	public PrimaryKeyMissingException(final Throwable arg0) {
		super(arg0);
	}

}
