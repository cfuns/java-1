package de.benjaminborbe.navigation.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.navigation.guice.NavigationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NavigationServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NavigationModulesMock());
		final NavigationServlet a = injector.getInstance(NavigationServlet.class);
		final NavigationServlet b = injector.getInstance(NavigationServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
