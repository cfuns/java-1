package de.benjaminborbe.worktime.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.gui.guice.WorktimeGuiModulesMock;
import de.benjaminborbe.worktime.gui.servlet.WorktimeGuiServlet;

public class WorktimeGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeGuiModulesMock());
		final WorktimeGuiServlet a = injector.getInstance(WorktimeGuiServlet.class);
		final WorktimeGuiServlet b = injector.getInstance(WorktimeGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
