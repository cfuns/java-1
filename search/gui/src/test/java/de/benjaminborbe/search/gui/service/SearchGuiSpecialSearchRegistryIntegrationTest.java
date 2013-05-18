package de.benjaminborbe.search.gui.service;

import com.google.inject.Injector;
import de.benjaminborbe.search.gui.guice.SearchGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class SearchGuiSpecialSearchRegistryIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchGuiModulesMock());
		assertNotNull(injector.getInstance(SearchGuiSpecialSearchRegistry.class));
	}

	@Test
	public void testRegisteredSearches() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchGuiModulesMock());
		final SearchGuiSpecialSearchRegistry registry = injector.getInstance(SearchGuiSpecialSearchRegistry.class);
		assertThat(registry, is(notNullValue()));
		assertThat(registry.getAll(), is(notNullValue()));
		assertThat(registry.getAll().size(), is(2));
	}
}
