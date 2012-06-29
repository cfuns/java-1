package de.benjaminborbe.calendar.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.calendar.gui.guice.CalendarGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CalendarGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CalendarGuiModulesMock());
		final CalendarGuiServlet a = injector.getInstance(CalendarGuiServlet.class);
		final CalendarGuiServlet b = injector.getInstance(CalendarGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
