package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

public interface ConfigurationCacheKeyBuilder {

	public String createConfigurationKey(ConfigurationDescription configurationDescription);
}
