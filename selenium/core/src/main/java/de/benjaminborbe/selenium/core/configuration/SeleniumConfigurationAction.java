package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import org.openqa.selenium.WebDriver;

public interface SeleniumConfigurationAction extends SeleniumConfiguration {

	void run(final WebDriver driver);

}
