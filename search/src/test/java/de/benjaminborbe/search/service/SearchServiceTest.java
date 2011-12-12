package de.benjaminborbe.search.service;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchServiceTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchService a = injector.getInstance(SearchService.class);
		final SearchService b = injector.getInstance(SearchService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
