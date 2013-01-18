package de.benjaminborbe.lunch.config;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class LunchConfigImplUnitTest {

	@Test
	public void testKeywords() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		EasyMock.expect(configurationService.getConfigurationValue(EasyMock.anyObject(ConfigurationDescriptionString.class))).andReturn("Essen").anyTimes();
		EasyMock.replay(configurationService);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final LunchConfigImpl config = new LunchConfigImpl(logger, configurationService, parseUtil);
		assertNotNull(config.getMittagNotifyKeywords());
		assertEquals(1, config.getMittagNotifyKeywords().size());
		assertEquals("Essen", config.getMittagNotifyKeywords().get(0));
	}
}
