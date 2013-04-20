package de.benjaminborbe.configuration.core.service;

import org.osgi.framework.BundleContext;

import javax.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.core.dao.ConfigurationRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class ConfigurationServiceTracker extends RegistryServiceTracker<ConfigurationDescription> {

	public ConfigurationServiceTracker(final ConfigurationRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
