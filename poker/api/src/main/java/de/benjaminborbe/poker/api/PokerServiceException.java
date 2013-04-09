package de.benjaminborbe.poker.api;

public class PokerServiceException extends Exception {

	private static final long serialVersionUID = -8585932409852682198L;

	public PokerServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public PokerServiceException(final String arg0) {
		super(arg0);
	}

	public PokerServiceException(final Throwable arg0) {
		super(arg0);
	}

}
