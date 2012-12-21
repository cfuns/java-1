package de.benjaminborbe.projectile.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ProjectileGuiReportServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiReportServlet a = injector.getInstance(ProjectileGuiReportServlet.class);
		final ProjectileGuiReportServlet b = injector.getInstance(ProjectileGuiReportServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
