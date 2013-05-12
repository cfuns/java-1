package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationXpathImpl implements SeleniumActionConfigurationXpath {

	private String message;

	private String xpath;

	public SeleniumActionConfigurationXpathImpl(final String message, final String xpath) {
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
