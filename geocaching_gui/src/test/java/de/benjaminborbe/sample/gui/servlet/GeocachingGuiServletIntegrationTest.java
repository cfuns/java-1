package de.benjaminborbe.sample.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.geocaching.gui.servlet.GeocachingGuiServlet;
import de.benjaminborbe.sample.gui.guice.GeocachingGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GeocachingGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GeocachingGuiModulesMock());
		final GeocachingGuiServlet a = injector.getInstance(GeocachingGuiServlet.class);
		final GeocachingGuiServlet b = injector.getInstance(GeocachingGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
