package de.benjaminborbe.search;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.search.SearchActivator;
import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchActivator o = injector.getInstance(SearchActivator.class);
		assertNotNull(o);
	}
}
