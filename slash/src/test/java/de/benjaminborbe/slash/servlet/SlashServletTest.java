package de.benjaminborbe.slash.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.slash.servlet.SlashServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SlashServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashServlet a = injector.getInstance(SlashServlet.class);
		final SlashServlet b = injector.getInstance(SlashServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
