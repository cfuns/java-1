package de.benjaminborbe.storage.api;

public class StorageException extends Exception {

	private static final long serialVersionUID = -580497272087851770L;

	public StorageException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public StorageException(final String message) {
		super(message);
	}

	public StorageException(final Throwable cause) {
		super(cause);
	}

}
