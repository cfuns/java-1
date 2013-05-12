package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.WebDriver;

public interface SeleniumConfigurationAction extends SeleniumConfiguration {

	void run(final WebDriver driver, SeleniumExecutionProtocolImpl seleniumExecutionProtocol);

}
