package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationPageContentImpl implements SeleniumActionConfigurationPageContent {

	private final String message;

	public SeleniumActionConfigurationPageContentImpl(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
