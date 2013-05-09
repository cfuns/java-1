package de.benjaminborbe.httpdownloader.core.util;

public class HttpDownloaderException extends Exception {

	private static final long serialVersionUID = 5863352871510279689L;

	public HttpDownloaderException(final String arg0) {
		super(arg0);
	}

	public HttpDownloaderException(final Throwable arg0) {
		super(arg0);
	}

	public HttpDownloaderException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
