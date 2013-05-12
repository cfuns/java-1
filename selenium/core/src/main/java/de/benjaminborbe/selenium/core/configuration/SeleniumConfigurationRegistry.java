package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SeleniumConfigurationRegistry extends RegistryBase<SeleniumConfiguration> {

	@Inject
	public SeleniumConfigurationRegistry(final SeleniumConfigurationSimple seleniumConfigurationSimple) {
		super(seleniumConfigurationSimple);
	}
}
