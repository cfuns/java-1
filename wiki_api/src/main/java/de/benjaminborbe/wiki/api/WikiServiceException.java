package de.benjaminborbe.wiki.api;

public class WikiServiceException extends Exception {

	private static final long serialVersionUID = -4299176723293485538L;

	public WikiServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public WikiServiceException(final String arg0) {
		super(arg0);
	}

	public WikiServiceException(final Throwable arg0) {
		super(arg0);
	}

}
