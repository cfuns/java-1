package de.benjaminborbe.confluence.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.confluence.gui.guice.ConfluenceGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfluenceGuiInstanceListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceGuiModulesMock());
		final ConfluenceGuiInstanceListServlet a = injector.getInstance(ConfluenceGuiInstanceListServlet.class);
		final ConfluenceGuiInstanceListServlet b = injector.getInstance(ConfluenceGuiInstanceListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
