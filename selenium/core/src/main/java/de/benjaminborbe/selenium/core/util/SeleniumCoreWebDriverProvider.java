package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.inject.Inject;
import javax.inject.Provider;

public class SeleniumCoreWebDriverProvider implements Provider<WebDriver> {

	private final SeleniumCoreConfig seleniumCoreConfig;

	private final ParseUtil parseUtil;

	private final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator;

	@Inject
	public SeleniumCoreWebDriverProvider(
		final SeleniumCoreConfig seleniumCoreConfig,
		final ParseUtil parseUtil,
		final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator
	) {
		this.seleniumCoreConfig = seleniumCoreConfig;
		this.parseUtil = parseUtil;
		this.seleniumRemoteWebDriverCreator = seleniumRemoteWebDriverCreator;
	}

	@Override
	public WebDriver get() {
		final String url = "http://" + seleniumCoreConfig.getSeleniumRemoteHost() + ":" + seleniumCoreConfig.getSeleniumRemotePort() + "/wd/hub";
		try {
			//new FirefoxDriver();
			final DesiredCapabilities capability = DesiredCapabilities.firefox();
			return seleniumRemoteWebDriverCreator.create(parseUtil.parseURL(url), capability);
		} catch (ParseException e) {
			throw new SeleniumCoreWebDriverCreationException("parse selenium url failed: " + url, e);
		}
	}
}
