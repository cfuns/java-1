package de.benjaminborbe.websearch.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.guice.WebsearchModulesMock;

public class ConfigurationDaoTest {

	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final ConfigurationDao configurationDao = injector.getInstance(ConfigurationDao.class);
		assertNotNull(configurationDao);
	}

}
