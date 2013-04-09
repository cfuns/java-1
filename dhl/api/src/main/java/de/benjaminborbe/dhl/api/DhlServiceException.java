package de.benjaminborbe.dhl.api;

public class DhlServiceException extends Exception {

	private static final long serialVersionUID = -704447988497073389L;

	public DhlServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public DhlServiceException(final String arg0) {
		super(arg0);
	}

	public DhlServiceException(final Throwable arg0) {
		super(arg0);
	}

}
