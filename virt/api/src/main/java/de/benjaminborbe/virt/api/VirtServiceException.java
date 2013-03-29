package de.benjaminborbe.virt.api;

public class VirtServiceException extends Exception {

	public VirtServiceException(final String message) {
		super(message);
	}

	public VirtServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public VirtServiceException(final Throwable cause) {
		super(cause);
	}
}
