package de.benjaminborbe.tools.json;

public class JSONParseException extends Exception {

	private static final long serialVersionUID = 8874844097229799319L;

	public JSONParseException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public JSONParseException(final String arg0) {
		super(arg0);
	}

	public JSONParseException(final Throwable arg0) {
		super(arg0);
	}

}
