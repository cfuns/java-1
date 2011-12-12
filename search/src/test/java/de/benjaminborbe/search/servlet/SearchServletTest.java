package de.benjaminborbe.search.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.search.servlet.SearchServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchServlet a = injector.getInstance(SearchServlet.class);
		final SearchServlet b = injector.getInstance(SearchServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
