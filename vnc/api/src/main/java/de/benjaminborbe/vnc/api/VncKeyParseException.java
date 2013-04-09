package de.benjaminborbe.vnc.api;

public class VncKeyParseException extends Exception {

	private static final long serialVersionUID = 5032239031929741285L;

	public VncKeyParseException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public VncKeyParseException(final String arg0) {
		super(arg0);
	}

	public VncKeyParseException(final Throwable arg0) {
		super(arg0);
	}

}
