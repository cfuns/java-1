package de.benjaminborbe.configuration.core.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class ConfigurationRegistry extends RegistryBase<ConfigurationDescription> {

	private final Logger logger;

	@Inject
	public ConfigurationRegistry(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void add(final ConfigurationDescription configuration) {
		logger.trace("add configuration to registry: " + configuration.getName());
		super.add(configuration);
	}

	public ConfigurationDescription get(final ConfigurationIdentifier configurationIdentifier) {
		for (final ConfigurationDescription configurationDescription : getAll()) {
			if (configurationIdentifier.equals(configurationDescription.getId())) {
				return configurationDescription;
			}
		}
		return null;
	}
}
