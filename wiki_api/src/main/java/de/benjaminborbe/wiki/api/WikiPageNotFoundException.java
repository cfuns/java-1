package de.benjaminborbe.wiki.api;

public class WikiPageNotFoundException extends Exception {

	private static final long serialVersionUID = -7684209079492991315L;

	public WikiPageNotFoundException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public WikiPageNotFoundException(final String arg0) {
		super(arg0);
	}

	public WikiPageNotFoundException(final Throwable arg0) {
		super(arg0);
	}

}
