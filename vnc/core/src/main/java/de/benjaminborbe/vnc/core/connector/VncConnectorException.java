package de.benjaminborbe.vnc.core.connector;

public class VncConnectorException extends Exception {

	private static final long serialVersionUID = 1059174348957031895L;

	public VncConnectorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public VncConnectorException(final String arg0) {
		super(arg0);
	}

	public VncConnectorException(final Throwable arg0) {
		super(arg0);
	}

}
