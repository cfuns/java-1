package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageContent;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumActionPageContent implements SeleniumAction<SeleniumActionConfigurationPageContent> {

	private final Logger logger;

	@Inject
	public SeleniumActionPageContent(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Class<SeleniumActionConfigurationPageContent> getType() {
		return SeleniumActionConfigurationPageContent.class;
	}

	@Override
	public boolean execute(
		final WebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationPageContent seleniumActionConfiguration
	) {
		logger.trace("pageSource: " + webDriver.getPageSource());
		return true;
	}
}
