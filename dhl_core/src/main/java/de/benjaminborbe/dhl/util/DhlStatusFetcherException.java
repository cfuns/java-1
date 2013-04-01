package de.benjaminborbe.dhl.util;

public class DhlStatusFetcherException extends Exception {

	private static final long serialVersionUID = 315217461662380739L;

	public DhlStatusFetcherException(final String arg0) {
		super(arg0);
	}

	public DhlStatusFetcherException(final Throwable arg0) {
		super(arg0);
	}

	public DhlStatusFetcherException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
