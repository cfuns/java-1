package de.benjaminborbe.storage.util;

public class StorageConnectionClosedException extends RuntimeException {

	private static final long serialVersionUID = -591326551859953392L;

	public StorageConnectionClosedException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public StorageConnectionClosedException(final String arg0) {
		super(arg0);
	}

	public StorageConnectionClosedException(final Throwable arg0) {
		super(arg0);
	}

}
