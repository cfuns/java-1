package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;

import javax.inject.Inject;

public class ConfigurationGetValueCache implements ConfigurationGetValue {

	private final ConfigurationGetValue configurationGetValue;

	private final CacheService cacheService;

	private final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder;

	@Inject
	public ConfigurationGetValueCache(
		final ConfigurationGetValue configurationGetValue,
		final CacheService cacheService,
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder
	) {
		this.configurationGetValue = configurationGetValue;
		this.cacheService = cacheService;
		this.configurationCacheKeyBuilder = configurationCacheKeyBuilder;
	}

	@Override
	public String getConfigurationValue(final ConfigurationDescription configuration) throws ConfigurationServiceException, CacheServiceException {
		final String configurationKey = configurationCacheKeyBuilder.createConfigurationKey(configuration);
		if (cacheService.contains(configurationKey)) {
			return cacheService.get(configurationKey);
		} else {
			return configurationGetValue.getConfigurationValue(configuration);
		}
	}
}
