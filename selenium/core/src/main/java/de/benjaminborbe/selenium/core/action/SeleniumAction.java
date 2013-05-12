package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.WebDriver;

public interface SeleniumAction<T extends SeleniumActionConfiguration> {

	Class<T> getType();

	boolean execute(WebDriver webDriver, SeleniumExecutionProtocolImpl seleniumExecutionProtocol, T seleniumActionConfiguration);
}
