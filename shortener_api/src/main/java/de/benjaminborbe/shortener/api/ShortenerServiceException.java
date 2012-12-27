package de.benjaminborbe.shortener.api;

public class ShortenerServiceException extends Exception {

	private static final long serialVersionUID = 8797846656102643386L;

	public ShortenerServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ShortenerServiceException(final String arg0) {
		super(arg0);
	}

	public ShortenerServiceException(final Throwable arg0) {
		super(arg0);
	}

}
