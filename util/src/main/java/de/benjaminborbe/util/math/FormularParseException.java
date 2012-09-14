package de.benjaminborbe.util.math;

public class FormularParseException extends Exception {

	private static final long serialVersionUID = 4429379330057743693L;

	public FormularParseException(final String expression, final Throwable exception) {
		super("parse expression failed: '" + expression + "'", exception);
	}

	public FormularParseException(final String expression) {
		super("parse expression failed: '" + expression + "'");
	}

}
