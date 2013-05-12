package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationPageInfoImpl implements SeleniumActionConfigurationPageInfo {

	private final String message;

	public SeleniumActionConfigurationPageInfoImpl(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
