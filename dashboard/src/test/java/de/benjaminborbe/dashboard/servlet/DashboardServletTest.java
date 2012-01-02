package de.benjaminborbe.dashboard.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.dashboard.guice.DashboardModulesMock;
import de.benjaminborbe.dashboard.servlet.DashboardServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DashboardServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DashboardModulesMock());
		final DashboardServlet a = injector.getInstance(DashboardServlet.class);
		final DashboardServlet b = injector.getInstance(DashboardServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
