package de.benjaminborbe.slash.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.slash.servlet.RobotsTxtServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class RobotsTxtServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final RobotsTxtServlet a = injector.getInstance(RobotsTxtServlet.class);
		final RobotsTxtServlet b = injector.getInstance(RobotsTxtServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
