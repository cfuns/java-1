package de.benjaminborbe.index.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.index.gui.guice.IndexGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexGuiModulesMock());
		final IndexGuiServlet a = injector.getInstance(IndexGuiServlet.class);
		final IndexGuiServlet b = injector.getInstance(IndexGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
