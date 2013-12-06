package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

public class SeleniumCoreWebDriverProvider implements Provider<SeleniumCoreWebDriver> {

	private final Logger logger;

	private final SeleniumCoreConfig seleniumCoreConfig;

	private final ParseUtil parseUtil;

	private final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator;

	private final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry;

	private final SeleniumLocalChromeWebDriverCreator seleniumLocalChromeWebDriverCreator;

	@Inject
	public SeleniumCoreWebDriverProvider(
			final Logger logger,
			final SeleniumCoreConfig seleniumCoreConfig,
			final ParseUtil parseUtil,
			final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator,
			final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry,
			final SeleniumLocalChromeWebDriverCreator seleniumLocalChromeWebDriverCreator) {
		this.logger = logger;
		this.seleniumCoreConfig = seleniumCoreConfig;
		this.parseUtil = parseUtil;
		this.seleniumRemoteWebDriverCreator = seleniumRemoteWebDriverCreator;
		this.seleniumCoreWebDriverRegistry = seleniumCoreWebDriverRegistry;
		this.seleniumLocalChromeWebDriverCreator = seleniumLocalChromeWebDriverCreator;
	}

	@Override
	public SeleniumCoreWebDriver get() {
		final String url = "http://" + seleniumCoreConfig.getSeleniumRemoteHost() + ":" + seleniumCoreConfig.getSeleniumRemotePort() + "/wd/hub";
		try {
			final WebDriver driver;
			if (Boolean.TRUE.equals(seleniumCoreConfig.getSeleniumLocal())) {
				logger.debug("create local driver");
				// driver = seleniumLocalFirefoxWebDriverCreator.create();
				driver = seleniumLocalChromeWebDriverCreator.create();
			}
			else {
				logger.debug("create remote driver");
				driver = seleniumRemoteWebDriverCreator.create(parseUtil.parseURL(url));
			}

			final SeleniumCoreWebDriver seleniumCoreWebDriver = new SeleniumCoreWebDriver(driver);
			seleniumCoreWebDriverRegistry.add(seleniumCoreWebDriver);
			return seleniumCoreWebDriver;
		}
		catch (final ParseException e) {
			throw new SeleniumCoreWebDriverCreationException("parse selenium url failed: " + url, e);
		}
	}
}
