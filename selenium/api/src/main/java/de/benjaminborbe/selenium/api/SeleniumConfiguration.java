package de.benjaminborbe.selenium.api;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;

import java.util.List;

public interface SeleniumConfiguration {

	SeleniumConfigurationIdentifier getId();

	String getName();

	List<SeleniumActionConfiguration> getActionConfigurations();

	Boolean getCloseWindow();
}
