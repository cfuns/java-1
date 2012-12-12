package de.benjaminborbe.monitoring.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringGuiLiveServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiLiveServlet a = injector.getInstance(MonitoringGuiLiveServlet.class);
		final MonitoringGuiLiveServlet b = injector.getInstance(MonitoringGuiLiveServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
