package de.benjaminborbe.virt.api;

public class VirtServiceException extends Exception {

	private static final long serialVersionUID = -5444024050197077001L;

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
