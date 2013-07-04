package de.benjaminborbe.geocaching.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.geocaching.gui.guice.GeocachingGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
