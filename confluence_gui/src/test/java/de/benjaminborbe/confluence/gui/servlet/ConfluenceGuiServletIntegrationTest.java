package de.benjaminborbe.confluence.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.confluence.gui.guice.ConfluenceGuiModulesMock;
import de.benjaminborbe.confluence.gui.servlet.ConfluenceGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfluenceGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceGuiModulesMock());
		final ConfluenceGuiServlet a = injector.getInstance(ConfluenceGuiServlet.class);
		final ConfluenceGuiServlet b = injector.getInstance(ConfluenceGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
