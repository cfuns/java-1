package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationExpectText implements SeleniumActionConfiguration {

	private final String xpath;

	private final String message;

	private final String text;

	public SeleniumActionConfigurationExpectText(final String message, final String xpath, final String text) {
		this.message = message;
		this.text = text;
		this.xpath = xpath;
	}

	public String getText() {
		return text;
	}

	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
