package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationServiceCacheUnitTest {

	@Test
	public void testCreateConfigurationIdentifier() throws ConfigurationServiceException {
		final String id = "1337";
		final ConfigurationIdentifier configurationIdentifier = EasyMock.createMock(ConfigurationIdentifier.class);
		final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.createConfigurationIdentifier(id)).andReturn(configurationIdentifier);
		final ConfigurationCache configurationCache = EasyMock.createMock(ConfigurationCache.class);
		final Object[] mocks = {configurationService, configurationCache, configurationIdentifier};
		EasyMock.replay(mocks);
		final ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService, configurationCache);
		assertThat(configurationServiceCache.createConfigurationIdentifier(id), is(configurationIdentifier));
		EasyMock.verify(mocks);
	}

	@Test
	public void testListConfigurations() throws ConfigurationServiceException {
		final Collection<ConfigurationDescription> list = Arrays.asList();
		final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.listConfigurations()).andReturn(list);
		final ConfigurationCache configurationCache = EasyMock.createMock(ConfigurationCache.class);
		final Object[] mocks = {configurationService, configurationCache};
		EasyMock.replay(mocks);
		final ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService, configurationCache);
		assertThat(configurationServiceCache.listConfigurations(), is(list));
		EasyMock.verify(mocks);
	}

	@Test
	public void testGetConfiguration() throws ConfigurationServiceException {
		final ConfigurationIdentifier configurationIdentifier = EasyMock.createMock(ConfigurationIdentifier.class);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.getConfiguration(configurationIdentifier)).andReturn(configurationDescription);
		final ConfigurationCache configurationCache = EasyMock.createMock(ConfigurationCache.class);
		final Object[] mocks = {configurationIdentifier, configurationDescription, configurationService, configurationCache};
		EasyMock.replay(mocks);
		final ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService, configurationCache);
		assertThat(configurationServiceCache.getConfiguration(configurationIdentifier), is(configurationDescription));
		EasyMock.verify(mocks);
	}
}
