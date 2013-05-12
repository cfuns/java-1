package de.benjaminborbe.selenium.core.service;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

import javax.inject.Singleton;

@Singleton
public class SeleniumConfigurationServiceTracker extends RegistryServiceTracker<SeleniumConfiguration> {

	public SeleniumConfigurationServiceTracker(final SeleniumConfigurationRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
