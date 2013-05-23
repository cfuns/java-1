package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.action.SeleniumAction;
import de.benjaminborbe.selenium.core.action.SeleniumActionRegistry;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumCoreActionExecutor {

	private final Logger logger;

	private final SeleniumActionRegistry seleniumActionRegistry;

	@Inject
	public SeleniumCoreActionExecutor(
		final Logger logger,
		final SeleniumActionRegistry seleniumActionRegistry
	) {
		this.logger = logger;
		this.seleniumActionRegistry = seleniumActionRegistry;
	}

	public boolean execute(
		final SeleniumCoreWebDriver driver,
		final SeleniumActionConfiguration seleniumActionConfiguration,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol
	) {
		final SeleniumAction action = seleniumActionRegistry.get(seleniumActionConfiguration.getClass());
		if (action == null) {
			final String msg = "no action for type " + seleniumActionConfiguration.getClass() + " found!";
			logger.warn(msg);
			seleniumExecutionProtocol.addError(msg);
			return false;
		}
		final boolean success = action.execute(driver, seleniumExecutionProtocol, seleniumActionConfiguration);
		if (!success) {
			logger.debug("action not successful => return");
			return false;
		}
		return true;
	}
}
