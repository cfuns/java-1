package de.benjaminborbe.selenium.core.util;

public class SeleniumCoreWebDriverCreationException extends RuntimeException {

	private static final long serialVersionUID = 1996082425102827313L;

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
