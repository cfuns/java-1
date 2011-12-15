package de.benjaminborbe.index.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TwentyfeetPerformanceServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final TwentyfeetPerformanceServlet a = injector.getInstance(TwentyfeetPerformanceServlet.class);
		final TwentyfeetPerformanceServlet b = injector.getInstance(TwentyfeetPerformanceServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
