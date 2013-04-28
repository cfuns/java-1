package de.benjaminborbe.lunch.config;

import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LunchConfigImplUnitTest {

	@Test
	public void testKeywords() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
		final ConfigurationCache configurationCache = EasyMock.createMock(ConfigurationCache.class);
		EasyMock.expect(configurationCache.get(EasyMock.anyObject(ConfigurationDescriptionString.class))).andReturn("Essen").anyTimes();
		final Object[] mocks = {logger, configurationService, configurationCache};
		EasyMock.replay(mocks);

		final ConfigurationServiceCache configurationServiceCache = new ConfigurationServiceCache(configurationService, configurationCache);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final LunchConfigImpl config = new LunchConfigImpl(logger, configurationService, parseUtil, configurationServiceCache);
		assertNotNull(config.getMittagNotifyKeywords());
		assertEquals(1, config.getMittagNotifyKeywords().size());
		assertEquals("Essen", config.getMittagNotifyKeywords().get(0));

		EasyMock.verify(mocks);
	}
}
