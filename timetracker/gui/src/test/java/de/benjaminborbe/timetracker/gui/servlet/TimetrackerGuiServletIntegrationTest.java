package de.benjaminborbe.timetracker.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.timetracker.gui.guice.TimetrackerGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimetrackerGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerGuiModulesMock());
		final TimetrackerGuiServlet a = injector.getInstance(TimetrackerGuiServlet.class);
		final TimetrackerGuiServlet b = injector.getInstance(TimetrackerGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
