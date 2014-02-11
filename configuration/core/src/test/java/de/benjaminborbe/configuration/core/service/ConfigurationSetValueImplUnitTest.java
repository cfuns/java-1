package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.core.dao.ConfigurationBean;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.storage.api.StorageException;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class ConfigurationSetValueImplUnitTest {

	@Test
	public void testSetConfigurationValue() throws ConfigurationServiceException, ValidationException, StorageException {
		final String value = "value123";
		final ConfigurationIdentifier configurationIdentifier = new ConfigurationIdentifier("confA");
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		final ConfigurationDao configurationDao = EasyMock.createMock(ConfigurationDao.class);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		EasyMock.expect(configurationDao.load(configurationIdentifier)).andReturn(configurationBean);
		EasyMock.expect(configurationDescription.getId()).andReturn(configurationIdentifier);
		EasyMock.expect(configurationDescription.validateValue(value)).andReturn(true);
		configurationBean.setValue(value);
		configurationDao.save(configurationBean);
		final Object[] mocks = new Object[]{logger, configurationDao, configurationDescription, configurationBean};
		EasyMock.replay(mocks);
		final ConfigurationSetValueImpl configurationSetValue = new ConfigurationSetValueImpl(logger, configurationDao);
		configurationSetValue.setConfigurationValue(configurationDescription, value);
		EasyMock.verify(mocks);
	}

}
