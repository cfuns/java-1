package de.benjaminborbe.configuration.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.tools.util.RegistryBase;

@Singleton
public class ConfigurationRegistry extends RegistryBase<Configuration<?>> {

	private final Logger logger;

	@Inject
	public ConfigurationRegistry(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void add(final Configuration<?> configuration) {
		logger.trace("add configuration to registry: " + configuration.getName());
		super.add(configuration);
	}
}
