package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationClickImpl implements SeleniumActionConfigurationClick {

	private String xpath;

	private String message;

	public SeleniumActionConfigurationClickImpl(final String message, final String xpath) {
		this.message = message;
		this.xpath = xpath;
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
