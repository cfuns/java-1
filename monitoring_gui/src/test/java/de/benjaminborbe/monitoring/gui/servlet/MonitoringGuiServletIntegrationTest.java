package de.benjaminborbe.monitoring.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiServlet a = injector.getInstance(MonitoringGuiServlet.class);
		final MonitoringGuiServlet b = injector.getInstance(MonitoringGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
