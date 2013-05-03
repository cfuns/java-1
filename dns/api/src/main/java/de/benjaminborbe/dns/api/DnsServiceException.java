package de.benjaminborbe.dns.api;

public class DnsServiceException extends Exception {

	private static final long serialVersionUID = 9201831234988670236L;

	public DnsServiceException(final Throwable cause) {
		super(cause);
	}

	public DnsServiceException(final String message) {
		super(message);
	}

	public DnsServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
