package de.benjaminborbe.calendar.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.calendar.guice.CalendarModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CalendarServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CalendarModulesMock());
		final CalendarServlet a = injector.getInstance(CalendarServlet.class);
		final CalendarServlet b = injector.getInstance(CalendarServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
