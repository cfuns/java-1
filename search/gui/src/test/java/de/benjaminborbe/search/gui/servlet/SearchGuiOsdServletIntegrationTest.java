package de.benjaminborbe.search.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.search.gui.guice.SearchGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchGuiOsdServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchGuiModulesMock());
		final SearchGuiOsdServlet a = injector.getInstance(SearchGuiOsdServlet.class);
		final SearchGuiOsdServlet b = injector.getInstance(SearchGuiOsdServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
