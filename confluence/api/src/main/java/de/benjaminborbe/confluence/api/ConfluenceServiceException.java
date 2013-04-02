package de.benjaminborbe.confluence.api;

public class ConfluenceServiceException extends Exception {

	private static final long serialVersionUID = -8486657395459651697L;

	public ConfluenceServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ConfluenceServiceException(final String arg0) {
		super(arg0);
	}

	public ConfluenceServiceException(final Throwable arg0) {
		super(arg0);
	}

}
