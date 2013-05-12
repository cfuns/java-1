package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationPageInfo implements SeleniumActionConfiguration {

	private final String message;

	public SeleniumActionConfigurationPageInfo(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
