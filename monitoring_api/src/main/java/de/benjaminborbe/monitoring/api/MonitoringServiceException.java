package de.benjaminborbe.monitoring.api;

public class MonitoringServiceException extends Exception {

	private static final long serialVersionUID = -6132535111607797448L;

	public MonitoringServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MonitoringServiceException(final String message) {
		super(message);
	}

	public MonitoringServiceException(final Throwable cause) {
		super(cause);
	}

}
