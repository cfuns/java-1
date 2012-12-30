package de.benjaminborbe.websearch.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.dao.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.guice.WebsearchModulesMock;

public class WebsearchConfigurationDaoIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final WebsearchConfigurationDao configurationDao = injector.getInstance(WebsearchConfigurationDao.class);
		assertNotNull(configurationDao);
	}

}
