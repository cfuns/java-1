package de.benjaminborbe.calendar.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.calendar.guice.CalendarModulesMock;
import de.benjaminborbe.calendar.servlet.CalendarServlet;
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

	@Test
	public void buildRedirectTargetPath() {
		final CalendarServlet calendarServlet = new CalendarServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", calendarServlet.buildRedirectTargetPath(request));
	}
}
