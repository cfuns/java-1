package de.benjaminborbe.tools.util;

public class ParseException extends Exception {

	private static final long serialVersionUID = -5714149974644131596L;

	public ParseException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ParseException(final String arg0) {
		super(arg0);
	}

	public ParseException(final Throwable arg0) {
		super(arg0);
	}

}
