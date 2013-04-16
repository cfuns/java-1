package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.core.guice.WebsearchModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class WebsearchConfigurationDaoIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final WebsearchConfigurationDao configurationDao = injector.getInstance(WebsearchConfigurationDao.class);
		assertNotNull(configurationDao);
	}

}
