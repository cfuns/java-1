package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationExpectTextImpl implements SeleniumActionConfigurationExpectText {

	private String xpath;

	private final String message;

	private String text;

	public SeleniumActionConfigurationExpectTextImpl(final String message, final String xpath, final String text) {
		this.message = message;
		this.text = text;
		this.xpath = xpath;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
