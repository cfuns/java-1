package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationFollowAttribute implements SeleniumActionConfiguration {

	private final String xpath;

	private final String message;

	private final String attribute;

	public SeleniumActionConfigurationFollowAttribute(final String message, final String xpath, final String attribute) {
		this.message = message;
		this.xpath = xpath;
		this.attribute = attribute;
	}

	public String getXpath() {
		return xpath;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getAttribute() {
		return attribute;
	}

}
