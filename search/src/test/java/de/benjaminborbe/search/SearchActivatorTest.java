package de.benjaminborbe.search;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchActivator o = injector.getInstance(SearchActivator.class);
		assertNotNull(o);
	}
}
