package de.benjaminborbe.timetracker.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.timetracker.gui.guice.TimetrackerGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

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
