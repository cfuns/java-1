package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.inject.Inject;
import javax.inject.Provider;

public class SeleniumCoreWebDriverProvider implements Provider<SeleniumCoreWebDriver> {

	private final SeleniumCoreConfig seleniumCoreConfig;

	private final ParseUtil parseUtil;

	private final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator;

	private final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry;

	@Inject
	public SeleniumCoreWebDriverProvider(
		final SeleniumCoreConfig seleniumCoreConfig,
		final ParseUtil parseUtil,
		final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator,
		final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry
	) {
		this.seleniumCoreConfig = seleniumCoreConfig;
		this.parseUtil = parseUtil;
		this.seleniumRemoteWebDriverCreator = seleniumRemoteWebDriverCreator;
		this.seleniumCoreWebDriverRegistry = seleniumCoreWebDriverRegistry;
	}

	@Override
	public SeleniumCoreWebDriver get() {
		final String url = "http://" + seleniumCoreConfig.getSeleniumRemoteHost() + ":" + seleniumCoreConfig.getSeleniumRemotePort() + "/wd/hub";
		try {
			//new FirefoxDriver();
			final DesiredCapabilities capability = DesiredCapabilities.firefox();
			final SeleniumCoreWebDriver seleniumCoreWebDriver = new SeleniumCoreWebDriver(seleniumRemoteWebDriverCreator.create(parseUtil.parseURL(url), capability));
			seleniumCoreWebDriverRegistry.add(seleniumCoreWebDriver);
			return seleniumCoreWebDriver;
		} catch (ParseException e) {
			throw new SeleniumCoreWebDriverCreationException("parse selenium url failed: " + url, e);
		}
	}
}
