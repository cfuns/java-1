package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationSendKeys implements SeleniumActionConfiguration {

	private final String xpath;

	private final String keys;

	private final String message;

	public String getKeys() {
		return keys;
	}

	public SeleniumActionConfigurationSendKeys(final String message, final String xpath, final String keys) {
		this.message = message;
		this.xpath = xpath;
		this.keys = keys;
	}

	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
