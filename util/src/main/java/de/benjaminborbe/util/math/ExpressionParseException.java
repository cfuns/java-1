package de.benjaminborbe.util.math;

public class ExpressionParseException extends Exception {

	private static final long serialVersionUID = 4429379330057743693L;

	public ExpressionParseException(final String expression, final Throwable exception) {
		super("parse expression failed: '" + expression + "'", exception);
	}

	public ExpressionParseException(final String expression) {
		super("parse expression failed: '" + expression + "'");
	}

}
