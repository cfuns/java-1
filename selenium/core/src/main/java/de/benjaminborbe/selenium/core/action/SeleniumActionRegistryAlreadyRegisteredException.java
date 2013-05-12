package de.benjaminborbe.selenium.core.action;

public class SeleniumActionRegistryAlreadyRegisteredException extends RuntimeException {

	public SeleniumActionRegistryAlreadyRegisteredException(final Throwable cause) {
		super(cause);
	}

	public SeleniumActionRegistryAlreadyRegisteredException(final String message) {
		super(message);
	}

	public SeleniumActionRegistryAlreadyRegisteredException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
