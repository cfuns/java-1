package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectUrl;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;

public class SeleniumActionExpectUrl implements SeleniumAction<SeleniumActionConfigurationExpectUrl> {

	@Override
	public Class<SeleniumActionConfigurationExpectUrl> getType() {
		return SeleniumActionConfigurationExpectUrl.class;
	}

	@Override
	public boolean execute(
		final SeleniumCoreWebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationExpectUrl seleniumActionConfiguration
	) {

		final String currentUrl = webDriver.getCurrentUrl();
		final String expectedUrl = seleniumActionConfiguration.getUrl().toExternalForm();
		if (currentUrl.equals(expectedUrl)) {
			seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage());
			return true;
		} else {
			seleniumExecutionProtocol.addError(seleniumActionConfiguration.getMessage() + ": expectedUrl  '" + expectedUrl + "' but was currentUrl '" + currentUrl + "'");
			return false;
		}
	}
}
