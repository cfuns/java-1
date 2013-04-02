package de.benjaminborbe.index.api;

public class IndexerServiceException extends Exception {

	private static final long serialVersionUID = -3778206002439441516L;

	public IndexerServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public IndexerServiceException(final String arg0) {
		super(arg0);
	}

	public IndexerServiceException(final Throwable arg0) {
		super(arg0);
	}

}
