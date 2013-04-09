package de.benjaminborbe.lunch.api;

public class LunchServiceException extends Exception {

	private static final long serialVersionUID = -6909868210952188791L;

	public LunchServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LunchServiceException(final String arg0) {
		super(arg0);
	}

	public LunchServiceException(final Throwable arg0) {
		super(arg0);
	}

}
