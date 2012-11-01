package de.benjaminborbe.task.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskListServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskGuiTaskListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiTaskListServlet a = injector.getInstance(TaskGuiTaskListServlet.class);
		final TaskGuiTaskListServlet b = injector.getInstance(TaskGuiTaskListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
