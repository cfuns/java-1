package de.benjaminborbe.cache.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.cache.gui.guice.CacheGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CacheGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CacheGuiModulesMock());
		final CacheGuiServlet a = injector.getInstance(CacheGuiServlet.class);
		final CacheGuiServlet b = injector.getInstance(CacheGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
