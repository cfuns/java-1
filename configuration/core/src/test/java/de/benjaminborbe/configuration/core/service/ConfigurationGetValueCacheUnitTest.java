package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConfigurationGetValueCacheUnitTest {

	@Test
	public void testGetValueCacheNotMatching() throws Exception {
		final String value = "value123";
		final String configurationKey = "key123";
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder = EasyMock.createMock(ConfigurationCacheKeyBuilder.class);
		final CacheService cacheService = EasyMock.createMock(CacheService.class);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final ConfigurationGetValue configurationGetValue = EasyMock.createMock(ConfigurationGetValue.class);

		EasyMock.expect(configurationCacheKeyBuilder.createConfigurationKey(configurationDescription)).andReturn(configurationKey);
		EasyMock.expect(cacheService.contains(configurationKey)).andReturn(false);
		EasyMock.expect(configurationGetValue.getConfigurationValue(configurationDescription)).andReturn(value);

		final Object[] mocks = new Object[]{configurationDescription, configurationGetValue, cacheService, configurationCacheKeyBuilder};
		EasyMock.replay(mocks);

		final ConfigurationGetValueCache configurationGetValueCache = new ConfigurationGetValueCache(configurationGetValue, cacheService, configurationCacheKeyBuilder);
		assertThat(configurationGetValueCache.getConfigurationValue(configurationDescription), is(value));
		EasyMock.verify(mocks);
	}

	@Test
	public void testGetValueCacheMatching() throws Exception {
		final String value = "value123";
		final String configurationKey = "key123";
		final ConfigurationCacheKeyBuilder configurationCacheKeyBuilder = EasyMock.createMock(ConfigurationCacheKeyBuilder.class);
		final CacheService cacheService = EasyMock.createMock(CacheService.class);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final ConfigurationGetValue configurationGetValue = EasyMock.createMock(ConfigurationGetValue.class);

		EasyMock.expect(configurationCacheKeyBuilder.createConfigurationKey(configurationDescription)).andReturn(configurationKey);
		EasyMock.expect(cacheService.contains(configurationKey)).andReturn(true);
		EasyMock.expect(cacheService.get(configurationKey)).andReturn(value);

		final Object[] mocks = new Object[]{configurationDescription, configurationGetValue, cacheService, configurationCacheKeyBuilder};
		EasyMock.replay(mocks);

		final ConfigurationGetValueCache configurationGetValueCache = new ConfigurationGetValueCache(configurationGetValue, cacheService, configurationCacheKeyBuilder);
		assertThat(configurationGetValueCache.getConfigurationValue(configurationDescription), is(value));
		EasyMock.verify(mocks);
	}
}
