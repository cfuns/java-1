package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;

public interface SeleniumActionRegistry {

	SeleniumAction<SeleniumActionConfiguration> get(final Class<? extends SeleniumActionConfiguration> seleniumActionConfigurationClass);

}
