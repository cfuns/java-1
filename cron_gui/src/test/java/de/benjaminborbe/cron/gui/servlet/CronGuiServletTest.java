package de.benjaminborbe.cron.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.cron.gui.guice.CronGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CronGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CronGuiModulesMock());
		final CronGuiServlet a = injector.getInstance(CronGuiServlet.class);
		final CronGuiServlet b = injector.getInstance(CronGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
