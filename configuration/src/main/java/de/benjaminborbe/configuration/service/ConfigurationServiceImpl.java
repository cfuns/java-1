package de.benjaminborbe.configuration.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.util.ConfigurationRegistry;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Logger logger;

	private final ConfigurationRegistry configurationRegistry;

	@Inject
	public ConfigurationServiceImpl(final Logger logger, final ConfigurationRegistry configurationRegistry) {
		this.logger = logger;
		this.configurationRegistry = configurationRegistry;
	}

	@Override
	public Collection<Configuration<?>> listConfigurations() {
		logger.trace("listConfigurations");
		return configurationRegistry.getAll();
	}

	@Override
	public <T> T getConfigurationValue(final Configuration<T> configuration) {
		logger.trace("getConfigurationValue");
		return configuration.getDefaultValue();
	}
}
