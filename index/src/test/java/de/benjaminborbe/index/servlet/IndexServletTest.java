package de.benjaminborbe.index.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class IndexServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexServlet a = injector.getInstance(IndexServlet.class);
		final IndexServlet b = injector.getInstance(IndexServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
