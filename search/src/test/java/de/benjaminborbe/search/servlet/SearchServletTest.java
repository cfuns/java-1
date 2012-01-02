package de.benjaminborbe.search.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchServlet a = injector.getInstance(SearchServlet.class);
		final SearchServlet b = injector.getInstance(SearchServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
