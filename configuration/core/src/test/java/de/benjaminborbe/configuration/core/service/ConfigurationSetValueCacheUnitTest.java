package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import org.easymock.EasyMock;
import org.junit.Test;

public class ConfigurationSetValueCacheUnitTest {

	@Test
	public void testSetValue() throws Exception {
		final String value = "value123";
		final CacheService cacheService = EasyMock.createNiceMock(CacheService.class);
		final ConfigurationDescription configurationDescription = EasyMock.createNiceMock(ConfigurationDescription.class);
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder = EasyMock.createNiceMock(ConfigurationCacheKeyBuilder.class);
		final ConfigurationSetValue configurationSetValue = EasyMock.createMock(ConfigurationSetValue.class);
		configurationSetValue.setConfigurationValue(configurationDescription, value);
		final Object[] mocks = new Object[]{configurationSetValue, configurationDescription};
		EasyMock.replay(mocks);
		final ConfigurationSetValueCache configurationSetValueCache = new ConfigurationSetValueCache(configurationSetValue, cacheService, configurationCacheKeyBuilder);
		configurationSetValueCache.setConfigurationValue(configurationDescription, value);
		EasyMock.verify(mocks);
	}

	@Test
	public void testSetValueUpdateCacheService() throws Exception {
		final String value = "value123";
		final String configurationKey = "key123";
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder = EasyMock.createMock(ConfigurationCacheKeyBuilder.class);
		final CacheService cacheService = EasyMock.createMock(CacheService.class);
		final ConfigurationSetValue configurationSetValue = EasyMock.createNiceMock(ConfigurationSetValue.class);
		EasyMock.expect(configurationCacheKeyBuilder.createConfigurationKey(configurationDescription)).andReturn(configurationKey);
		cacheService.set(configurationKey, value);
		final Object[] mocks = new Object[]{configurationSetValue, configurationDescription, cacheService, configurationCacheKeyBuilder};
		EasyMock.replay(mocks);
		final ConfigurationSetValueCache configurationSetValueCache = new ConfigurationSetValueCache(configurationSetValue, cacheService, configurationCacheKeyBuilder);
		configurationSetValueCache.setConfigurationValue(configurationDescription, value);
		EasyMock.verify(mocks);
	}
}
