package de.benjaminborbe.websearch.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.gui.guice.WebsearchGuiModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebsearchGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiServlet a = injector.getInstance(WebsearchGuiServlet.class);
		final WebsearchGuiServlet b = injector.getInstance(WebsearchGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
