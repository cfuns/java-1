package de.benjaminborbe.systemstatus.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.systemstatus.gui.guice.SystemstatusGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SystemstatusGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SystemstatusGuiModulesMock());
		final SystemstatusGuiServlet a = injector.getInstance(SystemstatusGuiServlet.class);
		final SystemstatusGuiServlet b = injector.getInstance(SystemstatusGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
