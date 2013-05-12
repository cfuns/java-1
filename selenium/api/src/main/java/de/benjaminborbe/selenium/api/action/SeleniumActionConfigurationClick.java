package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationClick implements SeleniumActionConfiguration {

	private String xpath;

	private String message;

	public SeleniumActionConfigurationClick(final String message, final String xpath) {
		this.message = message;
		this.xpath = xpath;
	}

	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
