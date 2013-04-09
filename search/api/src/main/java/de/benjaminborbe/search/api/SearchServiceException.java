package de.benjaminborbe.search.api;

public class SearchServiceException extends Exception {

	private static final long serialVersionUID = -6880048182167345401L;

	public SearchServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public SearchServiceException(final String arg0) {
		super(arg0);
	}

	public SearchServiceException(final Throwable arg0) {
		super(arg0);
	}

}
