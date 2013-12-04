package de.benjaminborbe.configuration.core.service;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.core.dao.ConfigurationBean;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationGetValueImplUnitTest {

	@Test
	public void testName() throws Exception {
		final String value = "value123";
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final ConfigurationDao configurationDao = EasyMock.createMock(ConfigurationDao.class);
		final ConfigurationGetValue configurationGetValue = new ConfigurationGetValueImpl(logger, configurationDao);
		final ConfigurationDescription configurationDescription = EasyMock.createMock(ConfigurationDescription.class);
		final ConfigurationIdentifier configurationIdentifier = EasyMock.createMock(ConfigurationIdentifier.class);
		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		EasyMock.expect(configurationDescription.getId()).andReturn(configurationIdentifier);
		EasyMock.expect(configurationDao.load(configurationIdentifier)).andReturn(configurationBean);
		EasyMock.expect(configurationBean.getValue()).andReturn(value);
		Object[] mocks = new Object[]{configurationDescription, logger, configurationDao, configurationIdentifier, configurationBean};
		EasyMock.replay(mocks);
		assertThat(configurationGetValue.getConfigurationValue(configurationDescription), is(value));
		EasyMock.verify(mocks);
	}
}
