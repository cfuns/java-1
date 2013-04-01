package de.benjaminborbe.authorization.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authorization.gui.guice.AuthorizationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class AuthorizationGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiServlet a = injector.getInstance(AuthorizationGuiServlet.class);
		final AuthorizationGuiServlet b = injector.getInstance(AuthorizationGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
