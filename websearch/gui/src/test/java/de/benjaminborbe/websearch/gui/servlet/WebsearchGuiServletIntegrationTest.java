package de.benjaminborbe.websearch.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.gui.guice.WebsearchGuiModulesMock;

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
