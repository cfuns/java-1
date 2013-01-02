package de.benjaminborbe.lucene.index.api;

public class LuceneIndexerServiceException extends Exception {

	private static final long serialVersionUID = -3778206002439441516L;

	public LuceneIndexerServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LuceneIndexerServiceException(final String arg0) {
		super(arg0);
	}

	public LuceneIndexerServiceException(final Throwable arg0) {
		super(arg0);
	}

}
