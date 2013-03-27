package de.benjaminborbe.tools.iterator;

public class IteratorException extends Exception {

	private static final long serialVersionUID = 621386731394786424L;

	public IteratorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public IteratorException(final String arg0) {
		super(arg0);
	}

	public IteratorException(final Throwable arg0) {
		super(arg0);
	}

}
