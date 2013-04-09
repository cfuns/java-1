package de.benjaminborbe.cache.api;

public class CacheServiceException extends Exception {

	private static final long serialVersionUID = 5140010443403391930L;

	public CacheServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public CacheServiceException(final String arg0) {
		super(arg0);
	}

	public CacheServiceException(final Throwable arg0) {
		super(arg0);
	}

}
