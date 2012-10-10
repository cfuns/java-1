package de.benjaminborbe.configuration.service;

import org.osgi.framework.BundleContext;

import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.util.ConfigurationRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class ConfigurationServiceTracker extends RegistryServiceTracker<ConfigurationDescription> {

	public ConfigurationServiceTracker(final ConfigurationRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
