package de.benjaminborbe.worktime.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.worktime.guice.WorktimeModulesMock;
import de.benjaminborbe.worktime.servlet.WorktimeServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WorktimeServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeServlet a = injector.getInstance(WorktimeServlet.class);
		final WorktimeServlet b = injector.getInstance(WorktimeServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
