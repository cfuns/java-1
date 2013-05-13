package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.action.SeleniumAction;
import de.benjaminborbe.selenium.core.action.SeleniumActionRegistry;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class SeleniumCoreExecutor {

	private final Logger logger;

	private final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider;

	private final SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	private final SeleniumActionRegistry seleniumActionRegistry;

	@Inject
	public SeleniumCoreExecutor(
		final Logger logger,
		final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider,
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry, final SeleniumActionRegistry seleniumActionRegistry
	) {
		this.logger = logger;
		this.seleniumCoreWebDriverProvider = seleniumCoreWebDriverProvider;
		this.seleniumConfigurationRegistry = seleniumConfigurationRegistry;
		this.seleniumActionRegistry = seleniumActionRegistry;
	}

	public void execute(final SeleniumConfiguration seleniumConfiguration, final SeleniumExecutionProtocolImpl seleniumExecutionProtocol) {
		WebDriver driver = null;
		try {
			driver = seleniumCoreWebDriverProvider.get();
			driver.manage().window().maximize();
			// http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			for (final SeleniumActionConfiguration seleniumActionConfiguration : seleniumConfiguration.getActionConfigurations()) {
				final SeleniumAction action = seleniumActionRegistry.get(seleniumActionConfiguration.getClass());
				if (action == null) {
					final String msg = "no action for type " + seleniumActionConfiguration.getClass() + " found!";
					logger.warn(msg);
					seleniumExecutionProtocol.addError(msg);
					return;
				}
				final boolean success = action.execute(driver, seleniumExecutionProtocol, seleniumActionConfiguration);
				if (!success) {
					logger.debug("action not successful => return");
					return;
				}
			}

			seleniumExecutionProtocol.addInfo("completed");
			seleniumExecutionProtocol.complete();
		} catch (Exception e) {
			logger.warn("Exception", e);
			seleniumExecutionProtocol.addError(e.getMessage());
		} finally {
			if (driver != null) {
				try {
					driver.close();
				} catch (Exception e) {
					logger.trace("close driver failed", e);
				}
			}
		}
	}
}
