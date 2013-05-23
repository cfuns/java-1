package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;

public interface SeleniumAction<T extends SeleniumActionConfiguration> {

	Class<T> getType();

	boolean execute(SeleniumCoreWebDriver webDriver, SeleniumExecutionProtocolImpl seleniumExecutionProtocol, T seleniumActionConfiguration);

}
