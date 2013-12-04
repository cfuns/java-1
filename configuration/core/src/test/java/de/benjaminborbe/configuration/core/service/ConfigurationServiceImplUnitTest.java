package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.configuration.core.dao.ConfigurationRegistry;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationServiceImplUnitTest {

	@Test
	public void testGetConfigurationValue() throws Exception {
		final String value = "value123";

		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final Logger logger = EasyMock.createMock(Logger.class);
		final ConfigurationRegistry configurationRegistry = EasyMock.createMock(ConfigurationRegistry.class);
		final ConfigurationDao configurationDao = EasyMock.createMock(ConfigurationDao.class);
		final ConfigurationSetValue configurationSetValue = EasyMock.createMock(ConfigurationSetValue.class);
		final ConfigurationGetValue configurationGetValue = EasyMock.createMock(ConfigurationGetValue.class);
		EasyMock.expect(configurationGetValue.getConfigurationValue(configurationDescription)).andReturn(value);

		Object[] mocks = new Object[]{configurationDescription, logger, configurationRegistry, configurationDao, configurationSetValue, configurationGetValue};
		EasyMock.replay(mocks);

		final ConfigurationService configurationService = new ConfigurationServiceImpl(logger, configurationRegistry, configurationDao, configurationSetValue, configurationGetValue);
		assertThat(configurationService.getConfigurationValue(configurationDescription), is(value));
		EasyMock.verify(mocks);
	}

	public void testSetConfigurationValue() throws Exception {
		final String value = "value123";

		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final Logger logger = EasyMock.createMock(Logger.class);
		final ConfigurationRegistry configurationRegistry = EasyMock.createMock(ConfigurationRegistry.class);
		final ConfigurationDao configurationDao = EasyMock.createMock(ConfigurationDao.class);
		final ConfigurationSetValue configurationSetValue = EasyMock.createMock(ConfigurationSetValue.class);
		final ConfigurationGetValue configurationGetValue = EasyMock.createMock(ConfigurationGetValue.class);
		configurationSetValue.setConfigurationValue(configurationDescription, value);

		Object[] mocks = new Object[]{configurationDescription, logger, configurationRegistry, configurationDao, configurationSetValue, configurationGetValue};
		EasyMock.replay(mocks);

		final ConfigurationService configurationService = new ConfigurationServiceImpl(logger, configurationRegistry, configurationDao, configurationSetValue, configurationGetValue);
		configurationService.setConfigurationValue(configurationDescription, value);
		EasyMock.verify(mocks);
	}
}
