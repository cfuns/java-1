package de.benjaminborbe.search.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.core.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchServiceIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchService a = injector.getInstance(SearchService.class);
		final SearchService b = injector.getInstance(SearchService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
