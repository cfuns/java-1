package de.benjaminborbe.projectile.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModulesMock;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ProjectileGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiServlet a = injector.getInstance(ProjectileGuiServlet.class);
		final ProjectileGuiServlet b = injector.getInstance(ProjectileGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
