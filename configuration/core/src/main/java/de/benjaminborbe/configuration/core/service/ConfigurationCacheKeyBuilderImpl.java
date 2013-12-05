package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import javax.inject.Inject;

public class ConfigurationCacheKeyBuilderImpl implements ConfigurationCacheKeyBuilder {

	public static final String PREFIX = "configuration";

	@Inject
	public ConfigurationCacheKeyBuilderImpl() {
	}

	public String createConfigurationKey(ConfigurationDescription configurationDescription) {
		return PREFIX + "-" + configurationDescription.getId().getId();
	}
}
