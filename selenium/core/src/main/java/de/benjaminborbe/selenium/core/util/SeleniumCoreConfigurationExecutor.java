package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.action.SeleniumActionRegistry;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class SeleniumCoreConfigurationExecutor {

	private final Logger logger;

	private final SeleniumCoreActionExecutor seleniumCoreActionExecutor;

	private final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider;

	private final SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	private final SeleniumActionRegistry seleniumActionRegistry;

	private final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry;

	@Inject
	public SeleniumCoreConfigurationExecutor(
		final Logger logger,
		final SeleniumCoreActionExecutor seleniumCoreActionExecutor,
		final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider,
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry,
		final SeleniumActionRegistry seleniumActionRegistry,
		final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry
	) {
		this.logger = logger;
		this.seleniumCoreActionExecutor = seleniumCoreActionExecutor;
		this.seleniumCoreWebDriverProvider = seleniumCoreWebDriverProvider;
		this.seleniumConfigurationRegistry = seleniumConfigurationRegistry;
		this.seleniumActionRegistry = seleniumActionRegistry;
		this.seleniumCoreWebDriverRegistry = seleniumCoreWebDriverRegistry;
	}

	public boolean execute(final SeleniumConfiguration seleniumConfiguration, final SeleniumExecutionProtocolImpl seleniumExecutionProtocol) {
		SeleniumCoreWebDriver driver = null;
		try {
			driver = seleniumCoreWebDriverProvider.get();
			driver.maximize();
			driver.implicitlyWait(10, TimeUnit.SECONDS);
			driver.deleteAllCookies();

			for (final SeleniumActionConfiguration seleniumActionConfiguration : seleniumConfiguration.getActionConfigurations()) {
				if (seleniumCoreActionExecutor.execute(driver, seleniumActionConfiguration, seleniumExecutionProtocol)) {
					logger.debug("execute action successful");
				} else {
					logger.debug("execute action failed");
					return false;
				}
			}

			seleniumExecutionProtocol.addInfo("completed");
			seleniumExecutionProtocol.complete();
			return true;
		} catch (Exception e) {
			logger.warn("Exception", e);
			seleniumExecutionProtocol.addError(e.getMessage());
			return false;
		} finally {
			if (driver != null) {
				try {
					if (seleniumConfiguration.getCloseWindow() == null || seleniumConfiguration.getCloseWindow()) {
						seleniumCoreWebDriverRegistry.remove(driver);
						driver.close();
					}
				} catch (Exception e) {
					logger.trace("close driver failed", e);
				}
			}
		}
	}
}
