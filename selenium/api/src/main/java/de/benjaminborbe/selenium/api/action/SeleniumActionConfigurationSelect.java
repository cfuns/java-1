package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationSelect implements SeleniumActionConfiguration {

	private final String xpath;

	private final String value;

	private final String message;

	public SeleniumActionConfigurationSelect(final String message, final String xpath, final String value) {
		this.message = message;
		this.xpath = xpath;
		this.value = value;
	}

	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getValue() {
		return value;
	}
}
