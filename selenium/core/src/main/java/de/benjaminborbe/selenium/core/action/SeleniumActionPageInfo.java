package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageInfo;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumActionPageInfo implements SeleniumAction<SeleniumActionConfigurationPageInfo> {

	private final Logger logger;

	@Inject
	public SeleniumActionPageInfo(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Class<SeleniumActionConfigurationPageInfo> getType() {
		return SeleniumActionConfigurationPageInfo.class;
	}

	@Override
	public boolean execute(
		final WebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationPageInfo seleniumActionConfiguration
	) {
		logger.debug("title: " + webDriver.getTitle());
		logger.debug("currentUrl: " + webDriver.getCurrentUrl());
		logger.debug("pageSource.length: " + webDriver.getPageSource().length());
		logger.debug("windowHandle: " + webDriver.getWindowHandle());
		logger.debug("windowHandles: " + webDriver.getWindowHandles());
		return true;
	}
}
