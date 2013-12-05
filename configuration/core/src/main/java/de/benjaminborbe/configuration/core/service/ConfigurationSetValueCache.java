package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

import javax.inject.Inject;

public class ConfigurationSetValueCache implements ConfigurationSetValue {

	private final ConfigurationSetValue configurationSetValue;

	private final CacheService cacheService;

	private final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder;

	@Inject
	public ConfigurationSetValueCache(
		final ConfigurationSetValue configurationSetValue,
		final CacheService cacheService,
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder
	) {
		this.configurationSetValue = configurationSetValue;
		this.cacheService = cacheService;
		this.configurationCacheKeyBuilder = configurationCacheKeyBuilder;
	}

	@Override
	public void setConfigurationValue(
		final ConfigurationDescription configurationDescription,
		final String value
	) throws CacheServiceException, ConfigurationServiceException, ValidationException {
		configurationSetValue.setConfigurationValue(configurationDescription, value);
		cacheService.set(configurationCacheKeyBuilder.createConfigurationKey(configurationDescription), value);
	}
}
