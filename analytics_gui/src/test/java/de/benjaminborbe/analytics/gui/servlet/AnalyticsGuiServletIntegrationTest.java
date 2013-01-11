package de.benjaminborbe.analytics.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.analytics.gui.guice.AnalyticsGuiModulesMock;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class AnalyticsGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AnalyticsGuiModulesMock());
		final AnalyticsGuiServlet a = injector.getInstance(AnalyticsGuiServlet.class);
		final AnalyticsGuiServlet b = injector.getInstance(AnalyticsGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
