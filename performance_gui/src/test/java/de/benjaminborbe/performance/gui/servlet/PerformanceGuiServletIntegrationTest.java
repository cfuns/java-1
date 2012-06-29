package de.benjaminborbe.performance.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.performance.gui.guice.PerformanceGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PerformanceGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PerformanceGuiModulesMock());
		final PerformanceGuiServlet a = injector.getInstance(PerformanceGuiServlet.class);
		final PerformanceGuiServlet b = injector.getInstance(PerformanceGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
