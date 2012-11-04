package de.benjaminborbe.storage.util;

public class StorageConnectionPoolException extends Exception {

	private static final long serialVersionUID = -2463349534423113630L;

	public StorageConnectionPoolException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public StorageConnectionPoolException(final String arg0) {
		super(arg0);
	}

	public StorageConnectionPoolException(final Throwable arg0) {
		super(arg0);
	}

}
