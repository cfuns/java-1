package de.benjaminborbe.wiki.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.wiki.gui.guice.WikiGuiModulesMock;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WikiGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WikiGuiModulesMock());
		final WikiGuiServlet a = injector.getInstance(WikiGuiServlet.class);
		final WikiGuiServlet b = injector.getInstance(WikiGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
