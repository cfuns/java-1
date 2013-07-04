package de.benjaminborbe.dashboard.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.dashboard.gui.guice.DashboardGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DashboardGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DashboardGuiModulesMock());
		final DashboardGuiServlet a = injector.getInstance(DashboardGuiServlet.class);
		final DashboardGuiServlet b = injector.getInstance(DashboardGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
