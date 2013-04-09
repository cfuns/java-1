package de.benjaminborbe.lucene.index.api;

public class LuceneIndexServiceException extends Exception {

	private static final long serialVersionUID = -3778206002439441516L;

	public LuceneIndexServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LuceneIndexServiceException(final String arg0) {
		super(arg0);
	}

	public LuceneIndexServiceException(final Throwable arg0) {
		super(arg0);
	}

}
