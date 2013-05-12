package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationAction;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumCoreExecutor {

	private final Logger logger;

	private final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider;

	private final SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	@Inject
	public SeleniumCoreExecutor(
		final Logger logger,
		final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider,
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry
	) {
		this.logger = logger;
		this.seleniumCoreWebDriverProvider = seleniumCoreWebDriverProvider;
		this.seleniumConfigurationRegistry = seleniumConfigurationRegistry;
	}

	public void execute(final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier, final SeleniumExecutionProtocolImpl seleniumExecutionProtocol) {
		WebDriver driver = null;
		try {
			driver = seleniumCoreWebDriverProvider.get();
			driver.manage().window().maximize();
			// http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp
			// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			SeleniumConfigurationAction seleniumConfiguration = seleniumConfigurationRegistry.get(seleniumConfigurationIdentifier);
			seleniumConfiguration.run(driver, seleniumExecutionProtocol);

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
