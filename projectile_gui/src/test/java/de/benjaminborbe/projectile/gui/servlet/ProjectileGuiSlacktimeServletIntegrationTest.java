package de.benjaminborbe.projectile.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ProjectileGuiSlacktimeServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiSlacktimeServlet a = injector.getInstance(ProjectileGuiSlacktimeServlet.class);
		final ProjectileGuiSlacktimeServlet b = injector.getInstance(ProjectileGuiSlacktimeServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
