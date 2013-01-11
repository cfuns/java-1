package de.benjaminborbe.projectile.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ProjectileGuiReportUserCurrentServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiReportUserCurrentServlet a = injector.getInstance(ProjectileGuiReportUserCurrentServlet.class);
		final ProjectileGuiReportUserCurrentServlet b = injector.getInstance(ProjectileGuiReportUserCurrentServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
