package de.benjaminborbe.storage.tools;

public class IdentifierIteratorException extends Exception {

	private static final long serialVersionUID = -7953755089776915597L;

	public IdentifierIteratorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public IdentifierIteratorException(final String arg0) {
		super(arg0);
	}

	public IdentifierIteratorException(final Throwable arg0) {
		super(arg0);
	}

}
