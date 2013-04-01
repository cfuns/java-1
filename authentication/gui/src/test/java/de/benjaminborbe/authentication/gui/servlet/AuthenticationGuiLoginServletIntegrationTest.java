package de.benjaminborbe.authentication.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.authentication.gui.guice.AuthenticationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthenticationGuiLoginServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationGuiModulesMock());
		final AuthenticationGuiLoginServlet a = injector.getInstance(AuthenticationGuiLoginServlet.class);
		final AuthenticationGuiLoginServlet b = injector.getInstance(AuthenticationGuiLoginServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
