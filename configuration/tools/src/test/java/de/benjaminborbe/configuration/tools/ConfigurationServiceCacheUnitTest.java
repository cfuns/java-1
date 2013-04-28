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
		// setup
		final String id = "1337";
		final ConfigurationIdentifier configurationIdentifier = EasyMock.createMock(ConfigurationIdentifier.class);
		ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.createConfigurationIdentifier(id)).andReturn(configurationIdentifier);
		EasyMock.replay(configurationService, configurationIdentifier);

		// test
		ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService);
		assertThat(configurationServiceCache.createConfigurationIdentifier(id), is(configurationIdentifier));
		EasyMock.verify(configurationService, configurationIdentifier);
	}

	@Test
	public void testListConfigurations() throws ConfigurationServiceException {
		// setup
		final Collection<ConfigurationDescription> list = Arrays.asList();
		ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.listConfigurations()).andReturn(list);
		EasyMock.replay(configurationService);

		// test
		ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService);
		assertThat(configurationServiceCache.listConfigurations(), is(list));
		EasyMock.verify(configurationService);
	}

	@Test
	public void testGetConfiguration() throws ConfigurationServiceException {
		// setup
		final ConfigurationIdentifier configurationIdentifier = EasyMock.createMock(ConfigurationIdentifier.class);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.getConfiguration(configurationIdentifier)).andReturn(configurationDescription);
		EasyMock.replay(configurationIdentifier, configurationDescription, configurationService);

		// test
		ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService);
		assertThat(configurationServiceCache.getConfiguration(configurationIdentifier), is(configurationDescription));
		EasyMock.verify(configurationIdentifier, configurationDescription, configurationService);
	}
}
