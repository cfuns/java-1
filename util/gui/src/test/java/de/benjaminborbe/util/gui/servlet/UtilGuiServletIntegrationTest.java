package de.benjaminborbe.util.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.util.gui.guice.UtilGuiModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilGuiModulesMock());
		final UtilGuiServlet a = injector.getInstance(UtilGuiServlet.class);
		final UtilGuiServlet b = injector.getInstance(UtilGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
