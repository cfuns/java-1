package de.benjaminborbe.crawler.api;

public class CrawlerException extends Exception {

	private static final long serialVersionUID = -6309787766597234595L;

	public CrawlerException(final String arg0) {
		super(arg0);
	}

	public CrawlerException(final Throwable arg0) {
		super(arg0);
	}

	public CrawlerException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
