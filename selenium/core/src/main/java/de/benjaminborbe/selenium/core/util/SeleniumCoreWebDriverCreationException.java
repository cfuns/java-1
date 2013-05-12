package de.benjaminborbe.selenium.core.util;

public class SeleniumCoreWebDriverCreationException extends RuntimeException {

	public SeleniumCoreWebDriverCreationException(final Throwable cause) {
		super(cause);
	}

	public SeleniumCoreWebDriverCreationException(final String message) {
		super(message);
	}

	public SeleniumCoreWebDriverCreationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
