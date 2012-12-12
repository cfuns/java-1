package de.benjaminborbe.configuration.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.configuration.gui.guice.ConfigurationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfigurationGuiListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfigurationGuiModulesMock());
		final ConfigurationGuiListServlet a = injector.getInstance(ConfigurationGuiListServlet.class);
		final ConfigurationGuiListServlet b = injector.getInstance(ConfigurationGuiListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
