package de.benjaminborbe.googlesearch.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.googlesearch.gui.guice.GooglesearchGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GooglesearchGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GooglesearchGuiModulesMock());
		final GooglesearchGuiServlet a = injector.getInstance(GooglesearchGuiServlet.class);
		final GooglesearchGuiServlet b = injector.getInstance(GooglesearchGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
