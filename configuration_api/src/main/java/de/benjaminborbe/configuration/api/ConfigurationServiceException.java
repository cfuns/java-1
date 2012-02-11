package de.benjaminborbe.configuration.api;

public class ConfigurationServiceException extends Exception {

	private static final long serialVersionUID = -55819287236679686L;

	public ConfigurationServiceException(final String message) {
		super(message);
	}

	public ConfigurationServiceException(final Throwable cause) {
		super(cause);
	}

	public ConfigurationServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
