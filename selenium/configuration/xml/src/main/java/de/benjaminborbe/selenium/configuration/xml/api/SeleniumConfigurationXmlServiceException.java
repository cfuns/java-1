package de.benjaminborbe.selenium.configuration.xml.api;

public class SeleniumConfigurationXmlServiceException extends Exception {

	private static final long serialVersionUID = 4495468847977233660L;

	public SeleniumConfigurationXmlServiceException(final Throwable cause) {
		super(cause);
	}

	public SeleniumConfigurationXmlServiceException(final String message) {
		super(message);
	}

	public SeleniumConfigurationXmlServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
