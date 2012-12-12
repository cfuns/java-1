package de.benjaminborbe.loggly.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.loggly.gui.guice.LogglyGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class LogglyGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LogglyGuiModulesMock());
		final LogglyGuiServlet a = injector.getInstance(LogglyGuiServlet.class);
		final LogglyGuiServlet b = injector.getInstance(LogglyGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
