package de.benjaminborbe.task.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.task.gui.servlet.TaskGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiServlet a = injector.getInstance(TaskGuiServlet.class);
		final TaskGuiServlet b = injector.getInstance(TaskGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
