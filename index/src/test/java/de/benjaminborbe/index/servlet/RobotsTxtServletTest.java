package de.benjaminborbe.index.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class RobotsTxtServletTest {

	@Test
	public void Singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final RobotsTxtServlet a = injector.getInstance(RobotsTxtServlet.class);
		final RobotsTxtServlet b = injector.getInstance(RobotsTxtServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
