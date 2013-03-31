package de.benjaminborbe.filestorage.api;

public class FilestorageServiceException extends Exception {

	public FilestorageServiceException(final Throwable cause) {
		super(cause);
	}

	public FilestorageServiceException(final String message) {
		super(message);
	}

	public FilestorageServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
