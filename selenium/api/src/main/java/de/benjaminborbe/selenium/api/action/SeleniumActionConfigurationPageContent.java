package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationPageContent implements SeleniumActionConfiguration {

	private final String message;

	public SeleniumActionConfigurationPageContent(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
