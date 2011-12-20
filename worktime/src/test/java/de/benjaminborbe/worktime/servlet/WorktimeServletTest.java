package de.benjaminborbe.worktime.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;

public class WorktimeServletTest {

	@Test
	public void Singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeServlet a = injector.getInstance(WorktimeServlet.class);
		final WorktimeServlet b = injector.getInstance(WorktimeServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
